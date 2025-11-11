package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Assignment;
import com.example.fu_academy.entity.Submission;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GradeInputViewModel extends AndroidViewModel {
    private EducationDatabase database;
    private ExecutorService executorService;

    private MutableLiveData<List<Assignment>> assignmentList = new MutableLiveData<>();
    private MutableLiveData<List<Submission>> submissionList = new MutableLiveData<>();
    private MutableLiveData<Map<Long, String>> studentNamesMap = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public GradeInputViewModel(@NonNull Application application) {
        super(application);
        database = EducationDatabase.getInstance(application);
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Assignment>> getAssignmentList() {
        return assignmentList;
    }

    public LiveData<List<Submission>> getSubmissionList() {
        return submissionList;
    }

    public LiveData<Map<Long, String>> getStudentNamesMap() {
        return studentNamesMap;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadAssignments(long classId) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                List<Assignment> assignments = database.assignmentDao().getByClass(classId);
                assignmentList.postValue(assignments);
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to load assignments: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void loadSubmissions(long assignmentId, long classId) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                // Get submissions for this assignment, filtered by students in this class
                List<Submission> submissions = database.submissionDao().getByAssignmentAndClass(assignmentId, classId);
                
                // Get all students enrolled in this class
                List<Enrollment> enrollments = database.enrollmentDao().getByClass(classId);
                
                // Build student names map
                Map<Long, String> studentNames = new HashMap<>();
                if (enrollments != null && !enrollments.isEmpty()) {
                    for (Enrollment enrollment : enrollments) {
                        User student = database.userDao().findById(enrollment.student_id);
                        if (student != null) {
                            studentNames.put(enrollment.student_id, student.name != null ? student.name : "Student " + enrollment.student_id);
                        }
                    }
                }
                
                // Create submissions for students who haven't submitted yet
                if (enrollments != null && !enrollments.isEmpty()) {
                    if (submissions == null) {
                        submissions = new ArrayList<>();
                    }
                    
                    for (Enrollment enrollment : enrollments) {
                        // Check if student already has a submission
                        boolean hasSubmission = false;
                        for (Submission sub : submissions) {
                            if (sub.student_id == enrollment.student_id) {
                                hasSubmission = true;
                                break;
                            }
                        }
                        
                        // If no submission exists, create a placeholder (not submitted yet)
                        if (!hasSubmission) {
                            Submission placeholder = new Submission();
                            placeholder.assignment_id = assignmentId;
                            placeholder.student_id = enrollment.student_id;
                            placeholder.grade = null;
                            placeholder.feedback = null;
                            placeholder.submit_date = null;
                            placeholder.file_name = null;
                            placeholder.file_url = null;
                            placeholder.status = "Not submitted";
                            // submission_id will be 0 for placeholder (not inserted yet)
                            placeholder.submission_id = 0;
                            
                            submissions.add(placeholder);
                        }
                    }
                }
                
                // Auto-grade 2-3 submissions with default scores if they don't have grades
                if (submissions != null && !submissions.isEmpty()) {
                    int countToGrade = Math.min(2 + (int)(Math.random() * 2), submissions.size()); // 2-3 submissions
                    int gradedCount = 0;
                    
                    for (Submission submission : submissions) {
                        // Only auto-grade if submission exists (not placeholder) and has no grade
                        if (submission.submission_id > 0 && submission.submit_date != null && submission.grade == null && gradedCount < countToGrade) {
                            // Assign default grade between 7.0 and 9.5
                            submission.grade = 7.0 + Math.random() * 2.5;
                            submission.feedback = "Đã chấm tự động";
                            database.submissionDao().update(submission);
                            gradedCount++;
                        }
                    }
                }
                
                studentNamesMap.postValue(studentNames);
                submissionList.postValue(submissions);
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to load submissions: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void saveGrades(List<Submission> updatedSubmissions, long classId) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                // Update or insert submissions with grades
                for (Submission submission : updatedSubmissions) {
                    // If submission_id is 0, it's a placeholder - skip (can't grade non-submitted work)
                    if (submission.submission_id == 0) {
                        continue;
                    }
                    
                    // Only update if grade or feedback is provided
                    if (submission.grade != null || (submission.feedback != null && !submission.feedback.isEmpty())) {
                        database.submissionDao().update(submission);
                    }
                }

                // Recalculate average grades for affected students
                updateAverageGrades(classId);

                successMessage.postValue("Grades saved successfully");
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to save grades: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    private void updateAverageGrades(long classId) {
        try {
            List<Enrollment> enrollments = database.enrollmentDao().getByClass(classId);

            for (Enrollment enrollment : enrollments) {
                // Get all submissions for this student in this class
                List<Submission> studentSubmissions = database.submissionDao()
                    .getByStudent(enrollment.student_id);

                // Calculate average grade
                double totalGrade = 0;
                int gradeCount = 0;

                for (Submission submission : studentSubmissions) {
                    if (submission.grade != null) {
                        totalGrade += submission.grade;
                        gradeCount++;
                    }
                }

                if (gradeCount > 0) {
                    enrollment.average_score = totalGrade / gradeCount;
                    database.enrollmentDao().update(enrollment);
                }
            }

        } catch (Exception e) {
            errorMessage.postValue("Failed to update average grades: " + e.getMessage());
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

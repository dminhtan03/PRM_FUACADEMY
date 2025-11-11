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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GradeInputViewModel extends AndroidViewModel {
    private EducationDatabase database;
    private ExecutorService executorService;

    private MutableLiveData<List<Assignment>> assignmentList = new MutableLiveData<>();
    private MutableLiveData<List<Submission>> submissionList = new MutableLiveData<>();
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

    public void loadSubmissions(long assignmentId) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                List<Submission> submissions = database.submissionDao().getByAssignment(assignmentId);
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
                // Update submissions with grades
                for (Submission submission : updatedSubmissions) {
                    database.submissionDao().update(submission);
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

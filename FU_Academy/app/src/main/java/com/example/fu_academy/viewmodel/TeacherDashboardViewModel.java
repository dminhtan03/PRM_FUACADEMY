package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.dto.TeacherDashboardSummary;
import com.example.fu_academy.dto.ClassInfo;
import com.example.fu_academy.dto.StudentInfo;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeacherDashboardViewModel extends AndroidViewModel {
    private EducationDatabase database;
    private ExecutorService executorService;

    private MutableLiveData<TeacherDashboardSummary> dashboardSummary = new MutableLiveData<>();
    private MutableLiveData<List<ClassInfo>> classList = new MutableLiveData<>();
    private MutableLiveData<List<StudentInfo>> studentList = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public TeacherDashboardViewModel(@NonNull Application application) {
        super(application);
        database = EducationDatabase.getInstance(application);
        executorService = Executors.newFixedThreadPool(4);
    }

    public LiveData<TeacherDashboardSummary> getDashboardSummary() {
        return dashboardSummary;
    }

    public LiveData<List<ClassInfo>> getClassList() {
        return classList;
    }

    public LiveData<List<StudentInfo>> getStudentList() {
        return studentList;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadDashboardData(long lecturerId) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                // Get course count
                List<Course> courses = database.courseDao().getCoursesByLecturer(lecturerId);
                int courseCount = courses.size();

                // Get student count
                int studentCount = database.enrollmentDao().countByLecturer(lecturerId);

                // Get pending submissions
                int pendingTasks = database.submissionDao().countPendingByLecturer(lecturerId);

                // Get average grade
                Double averageGrade = database.enrollmentDao().getAverageGradeByLecturer(lecturerId);
                if (averageGrade == null) averageGrade = 0.0;

                // Get feedback count
                int feedbackCount = database.feedbackDao().countByLecturer(lecturerId);

                // Get attendance rate
                Double attendanceRate = database.attendanceDetailDao().getAttendanceRateByLecturer(lecturerId);
                if (attendanceRate == null) attendanceRate = 0.0;

                // Get upcoming class
                List<Schedule> upcomingSchedules = database.scheduleDao().getUpcomingByLecturer(lecturerId);
                String upcomingClass = upcomingSchedules.isEmpty() ? "No upcoming classes" :
                    upcomingSchedules.get(0).date + " " + upcomingSchedules.get(0).time;

                TeacherDashboardSummary summary = new TeacherDashboardSummary(
                    courseCount, studentCount, pendingTasks, averageGrade,
                    feedbackCount, attendanceRate, upcomingClass
                );

                dashboardSummary.postValue(summary);
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to load dashboard data: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void loadMyClasses(long lecturerId) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                List<Class> classes = database.classDao().getByLecturer(lecturerId);
                List<ClassInfo> classInfoList = new ArrayList<>();

                for (Class clazz : classes) {
                    Course course = database.courseDao().findById(clazz.course_id);
                    List<Enrollment> enrollments = database.enrollmentDao().getByClass(clazz.class_id);

                    ClassInfo classInfo = new ClassInfo();
                    classInfo.setClassId(clazz.class_id);
                    classInfo.setClassCode("CL" + clazz.class_id);
                    classInfo.setCourseName(course != null ? course.name : "Unknown Course");
                    classInfo.setCourseCode(course != null ? course.course_code : "N/A");
                    classInfo.setRoom(clazz.room);
                    classInfo.setSchedule(clazz.schedule);
                    classInfo.setSemester(clazz.semester);
                    classInfo.setStudentCount(enrollments.size());
                    classInfo.setStatus("Active");

                    classInfoList.add(classInfo);
                }

                classList.postValue(classInfoList);
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to load classes: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void loadStudentsByClass(long classId) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                List<Enrollment> enrollments = database.enrollmentDao().getByClass(classId);
                List<StudentInfo> studentInfoList = new ArrayList<>();

                for (Enrollment enrollment : enrollments) {
                    User student = database.userDao().findById(enrollment.student_id);

                    // Calculate attendance rate for this specific class
                    int presentCount = database.attendanceDetailDao().getPresentCountByClass(enrollment.student_id, classId);
                    int totalCount = database.attendanceDetailDao().getTotalCountByClass(enrollment.student_id, classId);
                    double attendanceRate = totalCount > 0 ? (double) presentCount / totalCount * 100 : 0.0;

                    StudentInfo studentInfo = new StudentInfo();
                    studentInfo.setStudentId(enrollment.student_id);
                    studentInfo.setStudentName(student != null ? student.name : "Unknown Student");
                    studentInfo.setStudentEmail(student != null ? student.email : "N/A");
                    studentInfo.setStudentId_text("ST" + enrollment.student_id);
                    studentInfo.setAttendanceRate(attendanceRate);
                    studentInfo.setAverageGrade(enrollment.average_score);
                    studentInfo.setRemark(enrollment.remark != null ? enrollment.remark : "");

                    studentInfoList.add(studentInfo);
                }

                studentList.postValue(studentInfoList);
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to load students: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

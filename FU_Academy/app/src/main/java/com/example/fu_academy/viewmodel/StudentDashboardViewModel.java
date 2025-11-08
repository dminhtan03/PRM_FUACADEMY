package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.Notification;
import com.example.fu_academy.entity.Feedback;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentDashboardViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<Double> gpa = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalCredits = new MutableLiveData<>();
    private final MutableLiveData<String> semester = new MutableLiveData<>();
    private final MutableLiveData<Integer> notificationCount = new MutableLiveData<>();
    private final MutableLiveData<Schedule> upcomingClass = new MutableLiveData<>();
    private final MutableLiveData<Double> attendanceRate = new MutableLiveData<>();
    private final MutableLiveData<Integer> rank = new MutableLiveData<>();
    private final MutableLiveData<Integer> feedbackCount = new MutableLiveData<>();

    public StudentDashboardViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public void loadDashboardData(long studentId) {
        try {
            // Initialize default values
            gpa.postValue(0.0);
            totalCredits.postValue(0);
            semester.postValue("N/A");
            notificationCount.postValue(0);
            upcomingClass.postValue(null);
            attendanceRate.postValue(0.0);
            rank.postValue(0);
            feedbackCount.postValue(0);
            
            // Calculate GPA and Total Credits
            List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
            if (enrollments == null) {
                enrollments = new java.util.ArrayList<>();
            }
            
            double totalPoints = 0;
            int totalCreditsValue = 0;
            
            for (Enrollment e : enrollments) {
                if (e != null && e.grade != null && e.grade > 0) {
                    int credits = getCourseCredits(e.class_id);
                    totalCreditsValue += credits;
                    totalPoints += e.grade * credits;
                }
            }
            
            double gpaValue = totalCreditsValue > 0 ? totalPoints / totalCreditsValue : 0.0;
            gpa.postValue(gpaValue);
            totalCredits.postValue(totalCreditsValue);
            
            // Get current semester (simplified - you may want to get from enrollment)
            if (!enrollments.isEmpty()) {
                try {
                    Class firstClass = db.classDao().findById(enrollments.get(0).class_id);
                    if (firstClass != null && firstClass.semester != null) {
                        semester.postValue(firstClass.semester);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // Notification count
            try {
                List<Notification> notifications = db.notificationDao().getByRole("student");
                notificationCount.postValue(notifications != null ? notifications.size() : 0);
            } catch (Exception e) {
                e.printStackTrace();
                notificationCount.postValue(0);
            }
            
            // Upcoming class
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String today = sdf.format(new Date());
                List<Schedule> schedules = db.scheduleDao().getAll();
                Schedule nextClass = null;
                if (schedules != null) {
                    for (Schedule s : schedules) {
                        if (s != null && s.date != null && s.date.compareTo(today) >= 0 && 
                            (s.status == null || !s.status.equals("cancelled"))) {
                            if (nextClass == null || s.date.compareTo(nextClass.date) < 0) {
                                // Check if student is enrolled in this class
                                try {
                                    Class classObj = db.classDao().findById(s.class_id);
                                    if (classObj != null) {
                                        List<Enrollment> classEnrollments = db.enrollmentDao().getByClass(s.class_id);
                                        if (classEnrollments != null) {
                                            for (Enrollment en : classEnrollments) {
                                                if (en != null && en.student_id == studentId) {
                                                    nextClass = s;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                upcomingClass.postValue(nextClass);
            } catch (Exception e) {
                e.printStackTrace();
                upcomingClass.postValue(null);
            }
            
            // Attendance rate
            try {
                int presentCount = db.attendanceDetailDao().getPresentCount(studentId);
                int totalCount = db.attendanceDetailDao().getTotalCount(studentId);
                double attendanceRateValue = totalCount > 0 ? (double) presentCount / totalCount * 100 : 0.0;
                attendanceRate.postValue(attendanceRateValue);
            } catch (Exception e) {
                e.printStackTrace();
                attendanceRate.postValue(0.0);
            }
            
            // Feedback count
            try {
                List<Feedback> feedbacks = db.feedbackDao().getByUser(studentId);
                feedbackCount.postValue(feedbacks != null ? feedbacks.size() : 0);
            } catch (Exception e) {
                e.printStackTrace();
                feedbackCount.postValue(0);
            }
            
            // Rank (simplified - calculate based on GPA)
            rank.postValue(calculateRank(studentId, gpaValue));
        } catch (Exception e) {
            e.printStackTrace();
            // Set default values on error
            gpa.postValue(0.0);
            totalCredits.postValue(0);
            semester.postValue("N/A");
            notificationCount.postValue(0);
            upcomingClass.postValue(null);
            attendanceRate.postValue(0.0);
            rank.postValue(0);
            feedbackCount.postValue(0);
        }
    }

    private int getCourseCredits(long classId) {
        Class classObj = db.classDao().findById(classId);
        if (classObj != null) {
            Course course = db.courseDao().findById(classObj.course_id);
            if (course != null) {
                return course.credit;
            }
        }
        return 0;
    }

    private int calculateRank(long studentId, double gpa) {
        // Simplified rank calculation
        // In real implementation, you'd compare with all students
        return 1; // Placeholder
    }

    public LiveData<Double> getGpa() {
        return gpa;
    }

    public LiveData<Integer> getTotalCredits() {
        return totalCredits;
    }

    public LiveData<String> getSemester() {
        return semester;
    }

    public LiveData<Integer> getNotificationCount() {
        return notificationCount;
    }

    public LiveData<Schedule> getUpcomingClass() {
        return upcomingClass;
    }

    public LiveData<Double> getAttendanceRate() {
        return attendanceRate;
    }

    public LiveData<Integer> getRank() {
        return rank;
    }

    public LiveData<Integer> getFeedbackCount() {
        return feedbackCount;
    }
}


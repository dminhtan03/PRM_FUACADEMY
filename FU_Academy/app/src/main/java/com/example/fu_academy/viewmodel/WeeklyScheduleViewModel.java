package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.entity.Enrollment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyScheduleViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<List<ScheduleItem>> weeklySchedule = new MutableLiveData<>();

    public WeeklyScheduleViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public void loadWeeklySchedule(long studentId, String startDate) {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        
        android.util.Log.d("WeeklyScheduleVM", "Loading schedule for studentId: " + studentId + ", startDate: " + startDate);
        
        // Get all enrollments for the student
        List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
        android.util.Log.d("WeeklyScheduleVM", "Found " + (enrollments != null ? enrollments.size() : 0) + " enrollments");
        
        List<Long> classIds = new ArrayList<>();
        if (enrollments != null) {
            for (Enrollment e : enrollments) {
                classIds.add(e.class_id);
                android.util.Log.d("WeeklyScheduleVM", "Enrollment class_id: " + e.class_id);
            }
        }
        
        if (classIds.isEmpty()) {
            android.util.Log.w("WeeklyScheduleVM", "No enrollments found for studentId: " + studentId);
            weeklySchedule.postValue(scheduleItems);
            return;
        }
        
        // Get schedules for the week
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(startDate));
        } catch (Exception e) {
            android.util.Log.e("WeeklyScheduleVM", "Error parsing startDate: " + startDate, e);
            cal.setTime(new Date());
        }
        
        // Get end date (7 days later - thứ 7 của tuần)
        Calendar endCal = (Calendar) cal.clone();
        endCal.add(Calendar.DAY_OF_MONTH, 6); // Thứ 7 (6 ngày sau thứ 2)
        String endDate = sdf.format(endCal.getTime());
        
        android.util.Log.d("WeeklyScheduleVM", "Date range: " + startDate + " to " + endDate + " (Monday to Sunday)");
        
        // Get all schedules
        List<Schedule> allSchedules = db.scheduleDao().getAll();
        android.util.Log.d("WeeklyScheduleVM", "Total schedules in DB: " + (allSchedules != null ? allSchedules.size() : 0));
        
        if (allSchedules != null) {
            for (Schedule schedule : allSchedules) {
                android.util.Log.d("WeeklyScheduleVM", "Checking schedule: class_id=" + schedule.class_id + ", date=" + schedule.date + ", time=" + schedule.time);
                
                // Kiểm tra xem schedule có thuộc tuần này không (từ thứ 2 đến thứ 7)
                boolean isInWeek = false;
                if (schedule.date != null) {
                    try {
                        Date scheduleDate = sdf.parse(schedule.date);
                        Date weekStart = sdf.parse(startDate);
                        Date weekEnd = sdf.parse(endDate);
                        
                        // So sánh dates (bao gồm cả startDate và endDate)
                        isInWeek = (scheduleDate.compareTo(weekStart) >= 0 && scheduleDate.compareTo(weekEnd) <= 0);
                        
                        android.util.Log.d("WeeklyScheduleVM", "Schedule date " + schedule.date + " in week? " + isInWeek);
                    } catch (Exception e) {
                        android.util.Log.e("WeeklyScheduleVM", "Error parsing schedule date: " + schedule.date, e);
                    }
                }
                
                if (classIds.contains(schedule.class_id) && isInWeek) {
                    
                    android.util.Log.d("WeeklyScheduleVM", "Schedule matches! Adding to list");
                    
                    Class classObj = db.classDao().findById(schedule.class_id);
                    if (classObj != null) {
                        Course course = db.courseDao().findById(classObj.course_id);
                        User lecturer = db.userDao().getUserById(classObj.lecturer_id);
                        
                        ScheduleItem item = new ScheduleItem();
                        item.schedule = schedule;
                        item.courseName = course != null ? course.name : "N/A";
                        item.lecturerName = lecturer != null ? lecturer.name : "N/A";
                        item.room = schedule.room;
                        item.date = schedule.date;
                        item.time = schedule.time;
                        item.status = schedule.status;
                        item.type = schedule.type;
                        
                        scheduleItems.add(item);
                    } else {
                        android.util.Log.w("WeeklyScheduleVM", "Class not found for class_id: " + schedule.class_id);
                    }
                }
            }
        }
        
        android.util.Log.d("WeeklyScheduleVM", "Total schedule items found: " + scheduleItems.size());
        weeklySchedule.postValue(scheduleItems);
    }

    public LiveData<List<ScheduleItem>> getWeeklySchedule() {
        return weeklySchedule;
    }

    public static class ScheduleItem {
        public Schedule schedule;
        public String courseName;
        public String lecturerName;
        public String room;
        public String date;
        public String time;
        public String status;
        public String type;
    }
}


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
        
        // Get all enrollments for the student
        List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
        List<Long> classIds = new ArrayList<>();
        for (Enrollment e : enrollments) {
            classIds.add(e.class_id);
        }
        
        // Get schedules for the week
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(startDate));
        } catch (Exception e) {
            cal.setTime(new Date());
        }
        
        // Get end date (7 days later)
        Calendar endCal = (Calendar) cal.clone();
        endCal.add(Calendar.DAY_OF_MONTH, 7);
        String endDate = sdf.format(endCal.getTime());
        
        // Get all schedules
        List<Schedule> allSchedules = db.scheduleDao().getAll();
        
        for (Schedule schedule : allSchedules) {
            if (classIds.contains(schedule.class_id) && 
                schedule.date != null && 
                schedule.date.compareTo(startDate) >= 0 && 
                schedule.date.compareTo(endDate) < 0) {
                
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
                }
            }
        }
        
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


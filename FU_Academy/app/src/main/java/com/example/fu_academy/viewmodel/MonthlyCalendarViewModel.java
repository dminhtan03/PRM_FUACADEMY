package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.Exam;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Enrollment;

import java.util.ArrayList;
import java.util.List;

public class MonthlyCalendarViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<List<CalendarEvent>> monthlyEvents = new MutableLiveData<>();

    public MonthlyCalendarViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public void loadMonthlyEvents(long studentId, String month) {
        List<CalendarEvent> events = new ArrayList<>();
        
        // Get student enrollments
        List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
        List<Long> classIds = new ArrayList<>();
        for (Enrollment e : enrollments) {
            classIds.add(e.class_id);
        }
        
        // Get schedules for the month
        List<Schedule> allSchedules = db.scheduleDao().getAll();
        for (Schedule schedule : allSchedules) {
            if (classIds.contains(schedule.class_id) && 
                schedule.date != null && 
                schedule.date.startsWith(month)) {
                
                Class classObj = db.classDao().findById(schedule.class_id);
                if (classObj != null) {
                    Course course = db.courseDao().findById(classObj.course_id);
                    
                    CalendarEvent event = new CalendarEvent();
                    event.day = schedule.date;
                    event.courseName = course != null ? course.name : "N/A";
                    event.type = schedule.type;
                    event.room = schedule.room;
                    event.time = schedule.time;
                    event.status = schedule.status;
                    event.icon = getIconForType(schedule.type);
                    
                    events.add(event);
                }
            }
        }
        
        // Get exams for the month
        List<Exam> allExams = db.examDao().getAll();
        for (Exam exam : allExams) {
            if (exam.date != null && exam.date.startsWith(month)) {
                Course course = db.courseDao().findById(exam.course_id);
                
                CalendarEvent event = new CalendarEvent();
                event.day = exam.date;
                event.courseName = course != null ? course.name : "N/A";
                event.type = "exam";
                event.room = exam.room;
                event.time = exam.time;
                event.status = exam.status;
                event.icon = "exam";
                
                events.add(event);
            }
        }
        
        monthlyEvents.postValue(events);
    }

    private String getIconForType(String type) {
        if (type == null) return "class";
        switch (type.toLowerCase()) {
            case "lecture": return "lecture";
            case "lab": return "lab";
            case "exam": return "exam";
            default: return "class";
        }
    }

    public LiveData<List<CalendarEvent>> getMonthlyEvents() {
        return monthlyEvents;
    }

    public static class CalendarEvent {
        public String day;
        public String courseName;
        public String type;
        public String room;
        public String time;
        public String status;
        public String icon;
    }
}




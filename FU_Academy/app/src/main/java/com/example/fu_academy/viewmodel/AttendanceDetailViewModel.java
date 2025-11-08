package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.AttendanceDetail;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.User;

import java.util.ArrayList;
import java.util.List;

public class AttendanceDetailViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<List<AttendanceItem>> attendanceDetails = new MutableLiveData<>();
    private final MutableLiveData<Double> attendanceRate = new MutableLiveData<>();

    public AttendanceDetailViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public void loadAttendanceDetails(long studentId) {
        List<AttendanceItem> items = new ArrayList<>();
        
        List<AttendanceDetail> attendances = db.attendanceDetailDao().getByStudent(studentId);
        
        for (AttendanceDetail attendance : attendances) {
            Schedule schedule = null;
            for (Schedule s : db.scheduleDao().getAll()) {
                if (s.id == attendance.schedule_id) {
                    schedule = s;
                    break;
                }
            }
            
            if (schedule != null) {
                Class classObj = db.classDao().findById(schedule.class_id);
                if (classObj != null) {
                    Course course = db.courseDao().findById(classObj.course_id);
                    User lecturer = db.userDao().getUserById(classObj.lecturer_id);
                    
                    AttendanceItem item = new AttendanceItem();
                    item.attendance = attendance;
                    item.date = attendance.date;
                    item.courseName = course != null ? course.name : "N/A";
                    item.room = schedule.room;
                    item.status = attendance.status;
                    item.lecturerName = lecturer != null ? lecturer.name : "N/A";
                    item.remark = attendance.remark;
                    item.duration = attendance.duration;
                    item.type = attendance.type;
                    
                    items.add(item);
                }
            }
        }
        
        attendanceDetails.postValue(items);
        
        // Calculate attendance rate
        int presentCount = db.attendanceDetailDao().getPresentCount(studentId);
        int totalCount = db.attendanceDetailDao().getTotalCount(studentId);
        double rate = totalCount > 0 ? (double) presentCount / totalCount * 100 : 0.0;
        attendanceRate.postValue(rate);
    }

    public LiveData<List<AttendanceItem>> getAttendanceDetails() {
        return attendanceDetails;
    }

    public LiveData<Double> getAttendanceRate() {
        return attendanceRate;
    }

    public static class AttendanceItem {
        public AttendanceDetail attendance;
        public String date;
        public String courseName;
        public String room;
        public String status;
        public String lecturerName;
        public String remark;
        public int duration;
        public String type;
    }
}



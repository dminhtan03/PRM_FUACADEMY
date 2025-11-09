package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Exam;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.Class;

import java.util.ArrayList;
import java.util.List;

public class ExamScheduleViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<List<ExamItem>> examSchedule = new MutableLiveData<>();

    public ExamScheduleViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public void loadExamSchedule(long studentId) {
        List<ExamItem> examItems = new ArrayList<>();
        
        // Get student enrollments
        List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
        List<Long> courseIds = new ArrayList<>();
        for (Enrollment e : enrollments) {
            Class classObj = db.classDao().findById(e.class_id);
            if (classObj != null) {
                courseIds.add(classObj.course_id);
            }
        }
        
        // Get exams for enrolled courses
        List<Exam> allExams = db.examDao().getAll();
        for (Exam exam : allExams) {
            if (courseIds.contains(exam.course_id)) {
                Course course = db.courseDao().findById(exam.course_id);
                
                ExamItem item = new ExamItem();
                item.exam = exam;
                item.courseName = course != null ? course.name : "N/A";
                item.date = exam.date;
                item.time = exam.time;
                item.room = exam.room;
                item.seat = exam.seat;
                item.duration = exam.duration;
                item.type = exam.type;
                item.status = exam.status;
                
                examItems.add(item);
            }
        }
        
        examSchedule.postValue(examItems);
    }

    public LiveData<List<ExamItem>> getExamSchedule() {
        return examSchedule;
    }

    public static class ExamItem {
        public Exam exam;
        public String courseName;
        public String date;
        public String time;
        public String room;
        public String seat;
        public int duration;
        public String type;
        public String status;
    }
}





package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Course;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<List<Course>> courseList = new MutableLiveData<>();

    public CourseViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public LiveData<List<Course>> getCourses() {
        // Load all courses on background thread
        new Thread(() -> {
            List<Course> courses = db.courseDao().getAll();
            courseList.postValue(courses);
        }).start();
        return courseList;
    }

    public LiveData<List<Course>> getCoursesByStudent(long studentId) {
        courseList.postValue(db.courseDao().getCoursesByStudent(studentId));
        return courseList;
    }
}
package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    private ListView listView;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Danh Sách Khóa Học");
        }

        listView = findViewById(R.id.listViewCourses);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Get student ID from SharedPreferences (saved during login)
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", -1);

        // Load courses for the logged-in student
        if (studentId > 0) {
            courseViewModel.getCoursesByStudent(studentId).observe(this, courses -> {
                List<String> names = new ArrayList<>();
                if (courses != null && !courses.isEmpty()) {
                    for (Course c : courses) {
                        names.add(c.name + " - " + c.semester);
                    }
                } else {
                    names.add("Bạn chưa đăng ký khóa học nào");
                }
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
            });
        } else {
            // Fallback: show all courses if student_id is not available
            courseViewModel.getCourses().observe(this, courses -> {
                List<String> names = new ArrayList<>();
                for (Course c : courses) {
                    names.add(c.name + " - " + c.semester);
                }
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
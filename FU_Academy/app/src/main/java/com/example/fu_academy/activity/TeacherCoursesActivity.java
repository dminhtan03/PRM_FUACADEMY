package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.CourseAdapter;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.viewmodel.TeacherDashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class TeacherCoursesActivity extends BaseTeacherActivity {

    private TeacherDashboardViewModel viewModel;
    private CourseAdapter adapter;
    private RecyclerView recyclerView;
    private long lecturerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_courses);

        lecturerId = getIntent().getLongExtra("lecturer_id", -1);
        if (lecturerId == -1) {
            lecturerId = this.lecturerId; // Use from BaseTeacherActivity
        }

        initViews();
        setupViewModel();
        setupRecyclerView();
        loadCourses();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_courses);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(TeacherDashboardViewModel.class);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        EducationDatabase db = EducationDatabase.getInstance(this);
        adapter.setDatabase(db);
        adapter.setContext(this);
    }

    private void loadCourses() {
        new Thread(() -> {
            try {
                EducationDatabase db = EducationDatabase.getInstance(this);
                List<Course> courses = db.courseDao().getCoursesByLecturer(lecturerId);
                
                runOnUiThread(() -> {
                    if (courses != null && !courses.isEmpty()) {
                        adapter = new CourseAdapter(courses);
                        EducationDatabase db2 = EducationDatabase.getInstance(this);
                        adapter.setDatabase(db2);
                        adapter.setContext(this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Không có khóa học nào", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi khi tải danh sách khóa học: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }
}


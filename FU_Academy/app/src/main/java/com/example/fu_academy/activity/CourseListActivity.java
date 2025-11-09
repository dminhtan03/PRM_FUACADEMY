package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.CourseAdapter;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.viewmodel.CourseViewModel;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private CourseViewModel courseViewModel;
    private EducationDatabase db;

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
            getSupportActionBar().setTitle("Curriculum");
        }

        recyclerView = findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false); // Disable nested scrolling since it's inside ScrollView
        
        db = EducationDatabase.getInstance(this);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Load all courses (48 courses) - display all courses in the curriculum
        // Load directly from database on background thread to ensure all courses are loaded
        new Thread(() -> {
            List<Course> allCourses = db.courseDao().getAll();
            android.util.Log.d("CourseListActivity", "Total courses loaded: " + (allCourses != null ? allCourses.size() : 0));
            runOnUiThread(() -> {
                if (allCourses != null && !allCourses.isEmpty()) {
                    adapter = new CourseAdapter(allCourses);
                    adapter.setDatabase(db);
                    adapter.setContext(this);
                    recyclerView.setAdapter(adapter);
                    android.util.Log.d("CourseListActivity", "Adapter set with " + allCourses.size() + " courses");
                } else {
                    // Try using ViewModel as fallback
                    courseViewModel.getCourses().observe(this, courses -> {
                        if (courses != null && !courses.isEmpty()) {
                            adapter = new CourseAdapter(courses);
                            adapter.setDatabase(db);
                            adapter.setContext(this);
                            recyclerView.setAdapter(adapter);
                            android.util.Log.d("CourseListActivity", "Adapter set from ViewModel with " + courses.size() + " courses");
                        }
                    });
                }
            });
        }).start();
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
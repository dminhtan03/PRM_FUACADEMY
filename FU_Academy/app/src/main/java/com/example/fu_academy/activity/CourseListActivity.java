package com.example.fu_academy.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends ComponentActivity {
    private ListView listView;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        listView = findViewById(R.id.listViewCourses);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        courseViewModel.getCourses().observe(this, courses -> {
            List<String> names = new ArrayList<>();
            for (Course c : courses) {
                names.add(c.name + " - " + c.semester);
            }
            listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
        });
    }
}
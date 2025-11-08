package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.ExamAdapter;
import com.example.fu_academy.viewmodel.ExamScheduleViewModel;

public class ExamScheduleActivity extends ComponentActivity {
    private ExamScheduleViewModel viewModel;
    private RecyclerView recyclerView;
    private ExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);

        recyclerView = findViewById(R.id.recyclerViewExams);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExamAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ExamScheduleViewModel.class);

        // Get student ID
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", 1);

        // Load exam schedule
        viewModel.loadExamSchedule(studentId);

        // Observe data
        viewModel.getExamSchedule().observe(this, examItems -> {
            adapter.updateData(examItems);
        });
    }
}


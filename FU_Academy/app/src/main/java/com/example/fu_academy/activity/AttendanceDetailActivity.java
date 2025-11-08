package com.example.fu_academy.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.AttendanceAdapter;
import com.example.fu_academy.viewmodel.AttendanceDetailViewModel;

public class AttendanceDetailActivity extends ComponentActivity {
    private AttendanceDetailViewModel viewModel;
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private TextView tvAttendanceRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);

        recyclerView = findViewById(R.id.recyclerViewAttendance);
        tvAttendanceRate = findViewById(R.id.tvAttendanceRate);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendanceAdapter(null);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(AttendanceDetailViewModel.class);

        // Get student ID
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", 1);

        // Load attendance details
        viewModel.loadAttendanceDetails(studentId);

        // Observe data
        viewModel.getAttendanceDetails().observe(this, items -> {
            adapter.updateData(items);
        });

        viewModel.getAttendanceRate().observe(this, rate -> {
            if (rate != null) {
                tvAttendanceRate.setText(String.format("Tỷ lệ điểm danh: %.1f%%", rate));
            }
        });
    }
}


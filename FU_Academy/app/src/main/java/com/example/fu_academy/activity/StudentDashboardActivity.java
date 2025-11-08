package com.example.fu_academy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.viewmodel.StudentDashboardViewModel;

public class StudentDashboardActivity extends ComponentActivity {
    private StudentDashboardViewModel viewModel;
    private TextView tvGPA, tvTotalCredits, tvSemester, tvNotificationCount;
    private TextView tvUpcomingClass, tvAttendanceRate, tvRank, tvFeedbackCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        initViews();
        viewModel = new ViewModelProvider(this).get(StudentDashboardViewModel.class);

        // Get student ID from SharedPreferences or Intent
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", 1); // Default to 1 for demo

        // Load dashboard data
        viewModel.loadDashboardData(studentId);

        // Observe LiveData
        observeData();
    }

    private void initViews() {
        tvGPA = findViewById(R.id.tvGPA);
        tvTotalCredits = findViewById(R.id.tvTotalCredits);
        tvSemester = findViewById(R.id.tvSemester);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        tvUpcomingClass = findViewById(R.id.tvUpcomingClass);
        tvAttendanceRate = findViewById(R.id.tvAttendanceRate);
        tvRank = findViewById(R.id.tvRank);
        tvFeedbackCount = findViewById(R.id.tvFeedbackCount);
    }

    private void observeData() {
        viewModel.getGpa().observe(this, gpa -> {
            if (gpa != null) {
                tvGPA.setText(String.format("%.2f", gpa));
            }
        });

        viewModel.getTotalCredits().observe(this, credits -> {
            if (credits != null) {
                tvTotalCredits.setText(String.valueOf(credits));
            }
        });

        viewModel.getSemester().observe(this, semester -> {
            if (semester != null) {
                tvSemester.setText(semester);
            }
        });

        viewModel.getNotificationCount().observe(this, count -> {
            if (count != null) {
                tvNotificationCount.setText(String.valueOf(count));
            }
        });

        viewModel.getUpcomingClass().observe(this, schedule -> {
            if (schedule != null) {
                String classInfo = schedule.date + " " + schedule.time + " - " + schedule.room;
                tvUpcomingClass.setText(classInfo);
            } else {
                tvUpcomingClass.setText("Không có lớp sắp tới");
            }
        });

        viewModel.getAttendanceRate().observe(this, rate -> {
            if (rate != null) {
                tvAttendanceRate.setText(String.format("%.1f%%", rate));
            }
        });

        viewModel.getRank().observe(this, rank -> {
            if (rank != null) {
                tvRank.setText("#" + rank);
            }
        });

        viewModel.getFeedbackCount().observe(this, count -> {
            if (count != null) {
                tvFeedbackCount.setText(String.valueOf(count));
            }
        });
    }
}



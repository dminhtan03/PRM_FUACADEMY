package com.example.fu_academy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.viewmodel.StudentDashboardViewModel;

public class StudentDashboardActivity extends ComponentActivity {

    private StudentDashboardViewModel viewModel;
    private TextView tvGPA, tvTotalCredits, tvSemester, tvNotificationCount;
    private TextView tvUpcomingClass, tvAttendanceRate, tvRank, tvFeedbackCount;

    private Spinner spinnerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        initViews();

        viewModel = new ViewModelProvider(this).get(StudentDashboardViewModel.class);

        // Get student ID from SharedPreferences (saved during login)
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", -1);

        // Load dashboard data
        viewModel.loadDashboardData(studentId);

        // Observe LiveData
        observeData();

        // Setup Spinner drop-down menu
        setupSpinnerMenu();
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

        spinnerMenu = findViewById(R.id.spinnerMenu);
    }

    private void setupSpinnerMenu() {
        // Danh sách menu
        String[] menuItems = {
                "DashBoard",
                "Weekly Schedule",
                "Monthly Calendar",
                "Exam Schedule",
                "Attendance Detail",
                "Academic Summary",
                "Feedback Form"
        };

        // Gán ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, menuItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapter);

        // Listener
        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstSelect = true; // Bỏ qua trigger lần đầu
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelect) {
                    isFirstSelect = false;
                    return;
                }

                String selected = parent.getItemAtPosition(position).toString();

                switch (selected) {
                    case "Weekly Schedule":
                        startActivity(new Intent(StudentDashboardActivity.this, WeeklyScheduleActivity.class));
                        break;
                    case "Monthly Calendar":
                        startActivity(new Intent(StudentDashboardActivity.this, MonthlyCalendarActivity.class));
                        break;
                    case "Exam Schedule":
                        startActivity(new Intent(StudentDashboardActivity.this, ExamScheduleActivity.class));
                        break;
                    case "Attendance Detail":
                        startActivity(new Intent(StudentDashboardActivity.this, AttendanceDetailActivity.class));
                        break;
                    case "Academic Summary":
                        startActivity(new Intent(StudentDashboardActivity.this, AcademicSummaryActivity.class));
                        break;
                    case "Feedback Form":
                        startActivity(new Intent(StudentDashboardActivity.this, FeedbackFormActivity.class));
                        break;
                }

                // Reset Spinner về placeholder
                spinnerMenu.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void observeData() {
        viewModel.getGpa().observe(this, gpa -> {
            if (gpa != null) tvGPA.setText(String.format("%.2f", gpa));
        });

        viewModel.getTotalCredits().observe(this, credits -> {
            if (credits != null) tvTotalCredits.setText(String.valueOf(credits));
        });

        viewModel.getSemester().observe(this, semester -> {
            if (semester != null) tvSemester.setText(semester);
        });

        viewModel.getNotificationCount().observe(this, count -> {
            if (count != null) tvNotificationCount.setText(String.valueOf(count));
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
            if (rate != null) tvAttendanceRate.setText(String.format("%.1f%%", rate));
        });

        viewModel.getRank().observe(this, rank -> {
            if (rank != null) tvRank.setText("#" + rank);
        });

        viewModel.getFeedbackCount().observe(this, count -> {
            if (count != null) tvFeedbackCount.setText(String.valueOf(count));
        });
    }
}


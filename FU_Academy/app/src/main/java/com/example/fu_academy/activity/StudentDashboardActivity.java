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
                "Bảng điều khiển",
                "Lịch học theo tuần",
                "Lịch học theo tháng",
                "Lịch thi",
                "Báo cáo điểm danh",
                "Tổng kết học tập",
                "Ý kiến đánh giá",
                "Khung chương trình",
                "Điểm theo kỳ",
                "Danh sách bài tập",
                "Tài liệu môn học",
                "Bài tập đã nộp"
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
                    case "Lịch học theo tuần":
                        startActivity(new Intent(StudentDashboardActivity.this, WeeklyScheduleActivity.class));
                        break;
                    case "Lịch học theo tháng":
                        startActivity(new Intent(StudentDashboardActivity.this, MonthlyCalendarActivity.class));
                        break;
                    case "Lịch thi":
                        startActivity(new Intent(StudentDashboardActivity.this, ExamScheduleActivity.class));
                        break;
                    case "Báo cáo điểm danh":
                        startActivity(new Intent(StudentDashboardActivity.this, AttendanceDetailActivity.class));
                        break;
                    case "Tổng kết học tập":
                        startActivity(new Intent(StudentDashboardActivity.this, AcademicSummaryActivity.class));
                        break;
                    case "Ý kiến đánh giá":
                        startActivity(new Intent(StudentDashboardActivity.this, FeedbackFormActivity.class));
                        break;
                    case "Khung chương trình":
                        startActivity(new Intent(StudentDashboardActivity.this, CourseListActivity.class));
                        break;
                    case "Điểm theo kỳ":
                        startActivity(new Intent(StudentDashboardActivity.this, GradePerSemesterActivity.class));
                        break;
                    case "Danh sách bài tập":
                        Intent assignmentIntent = new Intent(StudentDashboardActivity.this, AssignmentListActivity.class);
                        assignmentIntent.putExtra("course_id", -1);
                        startActivity(assignmentIntent);
                        break;
                    case "Tài liệu môn học":
                        Intent materialIntent = new Intent(StudentDashboardActivity.this, MaterialListActivity.class);
                        materialIntent.putExtra("course_id", -1);
                        startActivity(materialIntent);
                        break;
                    case "Bài tập đã nộp":
                        startActivity(new Intent(StudentDashboardActivity.this, SubmissionListActivity.class));
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


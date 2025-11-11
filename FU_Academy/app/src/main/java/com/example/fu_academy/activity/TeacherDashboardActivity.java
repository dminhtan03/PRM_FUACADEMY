package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;
import androidx.core.content.ContextCompat;

import com.example.fu_academy.R;
import com.example.fu_academy.dto.TeacherDashboardSummary;
import com.example.fu_academy.viewmodel.TeacherDashboardViewModel;

import java.util.Locale;

public class TeacherDashboardActivity extends BaseTeacherActivity {

    private TeacherDashboardViewModel viewModel;

    // Dashboard summary views
    private TextView tvCourseCount, tvStudentCount, tvPendingTasks;
    private TextView tvAverageGrade, tvFeedbackCount, tvAttendanceRate;
    private TextView tvUpcomingClass;

    // Tab components
    private LinearLayout tabMyClasses, tabAttendance, tabGrade, tabUpload;
    private LinearLayout contentMyClasses, contentAttendance, contentGrade, contentUpload;
    private ImageView iconMyClasses, iconAttendance, iconGrade, iconUpload;
    private TextView labelMyClasses, labelAttendance, labelGrade, labelUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        initViews();
        setupViewModel();
        setupClickListeners();
        loadDashboardData();

        // Initialize with first tab selected
        switchTab(0);
    }

    private void initViews() {
        // Dashboard summary TextViews
        tvCourseCount = findViewById(R.id.tv_course_count);
        tvStudentCount = findViewById(R.id.tv_student_count);
        tvPendingTasks = findViewById(R.id.tv_pending_tasks);
        tvAverageGrade = findViewById(R.id.tv_average_grade);
        tvFeedbackCount = findViewById(R.id.tv_feedback_count);
        tvAttendanceRate = findViewById(R.id.tv_attendance_rate);
        tvUpcomingClass = findViewById(R.id.tv_upcoming_class);

        // Tab components
        tabMyClasses = findViewById(R.id.tab_my_classes);
        tabAttendance = findViewById(R.id.tab_attendance);
        tabGrade = findViewById(R.id.tab_grade);
        tabUpload = findViewById(R.id.tab_upload);

        // Tab content layouts
        contentMyClasses = findViewById(R.id.content_my_classes);
        contentAttendance = findViewById(R.id.content_attendance);
        contentGrade = findViewById(R.id.content_grade);
        contentUpload = findViewById(R.id.content_upload);

        // Tab icons and labels
        iconMyClasses = findViewById(R.id.icon_my_classes);
        iconAttendance = findViewById(R.id.icon_attendance);
        iconGrade = findViewById(R.id.icon_grade);
        iconUpload = findViewById(R.id.icon_upload);

        labelMyClasses = findViewById(R.id.label_my_classes);
        labelAttendance = findViewById(R.id.label_attendance);
        labelGrade = findViewById(R.id.label_grade);
        labelUpload = findViewById(R.id.label_upload);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(TeacherDashboardViewModel.class);

        // Observe dashboard summary
        viewModel.getDashboardSummary().observe(this, this::updateDashboard);

        // Observe error messages
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(this, isLoading -> {
            // You can show/hide a progress bar here
            // For now, we'll just handle it in the UI updates
        });
    }



    private void setupClickListeners() {
        // Tab bar navigation is handled by BaseTeacherActivity
        // Just handle tab content switching for dashboard preview
        tabMyClasses.setOnClickListener(v -> switchTab(0));
        tabAttendance.setOnClickListener(v -> switchTab(1));
        tabGrade.setOnClickListener(v -> switchTab(2));
        tabUpload.setOnClickListener(v -> switchTab(3));

        // Content area click listeners - navigate to actual activities
        contentMyClasses.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyClassesActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });

        contentAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(this, AttendanceSheetActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });

        contentGrade.setOnClickListener(v -> {
            Intent intent = new Intent(this, GradeInputActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });

        contentUpload.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadMaterialActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });
    }

    private void switchTab(int tabIndex) {
        // Hide all content layouts
        contentMyClasses.setVisibility(View.GONE);
        contentAttendance.setVisibility(View.GONE);
        contentGrade.setVisibility(View.GONE);
        contentUpload.setVisibility(View.GONE);

        // Reset all tab colors to inactive
        int inactiveColor = ContextCompat.getColor(this, R.color.bottom_nav_unselected);
        int activeColor = ContextCompat.getColor(this, R.color.bottom_nav_selected);

        iconMyClasses.setColorFilter(inactiveColor);
        iconAttendance.setColorFilter(inactiveColor);
        iconGrade.setColorFilter(inactiveColor);
        iconUpload.setColorFilter(inactiveColor);

        labelMyClasses.setTextColor(inactiveColor);
        labelAttendance.setTextColor(inactiveColor);
        labelGrade.setTextColor(inactiveColor);
        labelUpload.setTextColor(inactiveColor);

        // Show selected tab content and highlight tab
        switch (tabIndex) {
            case 0:
                contentMyClasses.setVisibility(View.VISIBLE);
                iconMyClasses.setColorFilter(activeColor);
                labelMyClasses.setTextColor(activeColor);
                break;
            case 1:
                contentAttendance.setVisibility(View.VISIBLE);
                iconAttendance.setColorFilter(activeColor);
                labelAttendance.setTextColor(activeColor);
                break;
            case 2:
                contentGrade.setVisibility(View.VISIBLE);
                iconGrade.setColorFilter(activeColor);
                labelGrade.setTextColor(activeColor);
                break;
            case 3:
                contentUpload.setVisibility(View.VISIBLE);
                iconUpload.setColorFilter(activeColor);
                labelUpload.setTextColor(activeColor);
                break;
        }
    }

    private void loadDashboardData() {
        viewModel.loadDashboardData(lecturerId);
    }

    private void updateDashboard(TeacherDashboardSummary summary) {
        if (summary != null) {
            tvCourseCount.setText(String.valueOf(summary.getCourseCount()));
            tvStudentCount.setText(String.valueOf(summary.getStudentCount()));
            tvPendingTasks.setText(String.valueOf(summary.getPendingTasks()));

            if (summary.getAverageGrade() != null) {
                tvAverageGrade.setText(String.format(Locale.getDefault(), "%.1f", summary.getAverageGrade()));
            } else {
                tvAverageGrade.setText("N/A");
            }

            tvFeedbackCount.setText(String.valueOf(summary.getFeedbackCount()));

            if (summary.getAttendanceRate() != null) {
                tvAttendanceRate.setText(String.format(Locale.getDefault(), "%.1f%%", summary.getAttendanceRate()));
            } else {
                tvAttendanceRate.setText("N/A");
            }

            tvUpcomingClass.setText(summary.getUpcomingClass());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        loadDashboardData();
    }
}

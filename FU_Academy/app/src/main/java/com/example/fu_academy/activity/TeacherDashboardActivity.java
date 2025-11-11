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

    // Tab components (for preview in dashboard)
    private LinearLayout tabMyClasses, tabCourse, tabSettings;
    private LinearLayout contentMyClasses, contentCourse, contentSettings;
    private ImageView iconMyClasses, iconCourse, iconSettings;
    private TextView labelMyClasses, labelCourse, labelSettings;

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

        // Tab components (for preview in dashboard)
        tabMyClasses = findViewById(R.id.tab_my_classes);
        tabCourse = findViewById(R.id.tab_course);
        tabSettings = findViewById(R.id.tab_settings);

        // Tab content layouts
        contentMyClasses = findViewById(R.id.content_my_classes);
        contentCourse = findViewById(R.id.content_course);
        contentSettings = findViewById(R.id.content_settings);

        // Tab icons and labels
        iconMyClasses = findViewById(R.id.icon_my_classes);
        iconCourse = findViewById(R.id.icon_course);
        iconSettings = findViewById(R.id.icon_settings);

        labelMyClasses = findViewById(R.id.label_my_classes);
        labelCourse = findViewById(R.id.label_course);
        labelSettings = findViewById(R.id.label_settings);
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
        // Note: Bottom navigation tabs are handled by BaseTeacherActivity
        // These are for the preview tabs in the dashboard content area only
        // Do not override bottom navigation behavior here

        // Content area click listeners - navigate to actual activities
        if (contentMyClasses != null) {
            contentMyClasses.setOnClickListener(v -> {
                Intent intent = new Intent(this, MyClassesActivity.class);
                intent.putExtra("lecturer_id", lecturerId);
                startActivity(intent);
            });
        }

        if (contentCourse != null) {
            contentCourse.setOnClickListener(v -> {
                Intent intent = new Intent(this, TeacherCoursesActivity.class);
                intent.putExtra("lecturer_id", lecturerId);
                startActivity(intent);
            });
        }

        if (contentSettings != null) {
            contentSettings.setOnClickListener(v -> {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("lecturer_id", lecturerId);
                startActivity(intent);
            });
        }
    }

    private void switchTab(int tabIndex) {
        // Hide all content layouts
        if (contentMyClasses != null) contentMyClasses.setVisibility(View.GONE);
        if (contentCourse != null) contentCourse.setVisibility(View.GONE);
        if (contentSettings != null) contentSettings.setVisibility(View.GONE);

        // Reset all tab colors to inactive
        int inactiveColor = ContextCompat.getColor(this, R.color.bottom_nav_unselected);
        int activeColor = ContextCompat.getColor(this, R.color.bottom_nav_selected);

        if (iconMyClasses != null) iconMyClasses.setColorFilter(inactiveColor);
        if (iconCourse != null) iconCourse.setColorFilter(inactiveColor);
        if (iconSettings != null) iconSettings.setColorFilter(inactiveColor);

        if (labelMyClasses != null) labelMyClasses.setTextColor(inactiveColor);
        if (labelCourse != null) labelCourse.setTextColor(inactiveColor);
        if (labelSettings != null) labelSettings.setTextColor(inactiveColor);

        // Show selected tab content and highlight tab
        switch (tabIndex) {
            case 0:
                if (contentMyClasses != null) contentMyClasses.setVisibility(View.VISIBLE);
                if (iconMyClasses != null) iconMyClasses.setColorFilter(activeColor);
                if (labelMyClasses != null) labelMyClasses.setTextColor(activeColor);
                break;
            case 1:
                if (contentCourse != null) contentCourse.setVisibility(View.VISIBLE);
                if (iconCourse != null) iconCourse.setColorFilter(activeColor);
                if (labelCourse != null) labelCourse.setTextColor(activeColor);
                break;
            case 2:
                if (contentSettings != null) contentSettings.setVisibility(View.VISIBLE);
                if (iconSettings != null) iconSettings.setColorFilter(activeColor);
                if (labelSettings != null) labelSettings.setTextColor(activeColor);
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        loadDashboardData();
    }
}

package com.example.fu_academy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.cardview.widget.CardView;

import com.example.fu_academy.R;
import com.example.fu_academy.dto.TeacherDashboardSummary;
import com.example.fu_academy.helper.SharedPreferencesHelper;
import com.example.fu_academy.viewmodel.TeacherDashboardViewModel;

public class TeacherDashboardActivity extends AppCompatActivity {

    private TeacherDashboardViewModel viewModel;
    private long lecturerId;

    // Dashboard summary views
    private TextView tvCourseCount, tvStudentCount, tvPendingTasks;
    private TextView tvAverageGrade, tvFeedbackCount, tvAttendanceRate;
    private TextView tvUpcomingClass;

    // Action buttons
    private CardView cardMyClasses, cardTakeAttendance, cardGradeInput, cardUploadMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        initViews();
        setupViewModel();
        getUserId();
        setupClickListeners();
        loadDashboardData();
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

        // Action cards
        cardMyClasses = findViewById(R.id.card_my_classes);
        cardTakeAttendance = findViewById(R.id.card_take_attendance);
        cardGradeInput = findViewById(R.id.card_grade_input);
        cardUploadMaterial = findViewById(R.id.card_upload_material);
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

    private void getUserId() {
        SharedPreferencesHelper prefs = new SharedPreferencesHelper(this);
        lecturerId = prefs.getUserId(); // đọc đúng nơi HomeActivity đang ghi
        if (lecturerId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void setupClickListeners() {
        cardMyClasses.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyClassesActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });

        cardTakeAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(this, AttendanceSheetActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });

        cardGradeInput.setOnClickListener(v -> {
            Intent intent = new Intent(this, GradeInputActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });

        cardUploadMaterial.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadMaterialActivity.class);
            intent.putExtra("lecturer_id", lecturerId);
            startActivity(intent);
        });
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
                tvAverageGrade.setText(String.format("%.1f", summary.getAverageGrade()));
            } else {
                tvAverageGrade.setText("N/A");
            }

            tvFeedbackCount.setText(String.valueOf(summary.getFeedbackCount()));

            if (summary.getAttendanceRate() != null) {
                tvAttendanceRate.setText(String.format("%.1f%%", summary.getAttendanceRate()));
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

package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fu_academy.R;
import com.example.fu_academy.helper.SharedPreferencesHelper;

public abstract class BaseTeacherActivity extends AppCompatActivity {

    protected long lecturerId;

    // Bottom navigation components
    private LinearLayout bottomNavigationContainer;
    private LinearLayout tabDashboard, tabMyClasses, tabAttendance, tabGrade, tabUpload;
    private ImageView iconDashboard, iconMyClasses, iconAttendance, iconGrade, iconUpload;
    private TextView labelDashboard, labelMyClasses, labelAttendance, labelGrade, labelUpload;

    // Current tab index for highlighting
    private int currentTabIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserId();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupBottomNavigation();
    }

    private void getUserId() {
        SharedPreferencesHelper prefs = new SharedPreferencesHelper(this);
        lecturerId = prefs.getUserId();
        if (lecturerId == -1) {
            // Handle case where user is not logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void setupBottomNavigation() {
        // Find bottom navigation components
        bottomNavigationContainer = findViewById(R.id.bottom_navigation_container);
        if (bottomNavigationContainer == null) return; // Activity doesn't have bottom nav

        tabDashboard = findViewById(R.id.tab_dashboard);
        tabMyClasses = findViewById(R.id.tab_my_classes);
        tabAttendance = findViewById(R.id.tab_attendance);
        tabGrade = findViewById(R.id.tab_grade);
        tabUpload = findViewById(R.id.tab_upload);

        iconDashboard = findViewById(R.id.icon_dashboard);
        iconMyClasses = findViewById(R.id.icon_my_classes);
        iconAttendance = findViewById(R.id.icon_attendance);
        iconGrade = findViewById(R.id.icon_grade);
        iconUpload = findViewById(R.id.icon_upload);

        labelDashboard = findViewById(R.id.label_dashboard);
        labelMyClasses = findViewById(R.id.label_my_classes);
        labelAttendance = findViewById(R.id.label_attendance);
        labelGrade = findViewById(R.id.label_grade);
        labelUpload = findViewById(R.id.label_upload);

        // Set up click listeners
        setupBottomNavClickListeners();

        // Highlight current tab based on activity
        highlightCurrentTab();
    }

    private void setupBottomNavClickListeners() {
        if (tabDashboard != null) {
            tabDashboard.setOnClickListener(v -> navigateToTab(-1)); // Dashboard
        }
        if (tabMyClasses != null) {
            tabMyClasses.setOnClickListener(v -> navigateToTab(0));
        }
        if (tabAttendance != null) {
            tabAttendance.setOnClickListener(v -> navigateToTab(1));
        }
        if (tabGrade != null) {
            tabGrade.setOnClickListener(v -> navigateToTab(2));
        }
        if (tabUpload != null) {
            tabUpload.setOnClickListener(v -> navigateToTab(3));
        }
    }

    private void navigateToTab(int tabIndex) {
        String currentClassName = this.getClass().getSimpleName();

        // Handle dashboard navigation
        if (tabIndex == -1) {
            if (currentClassName.equals("TeacherDashboardActivity")) return; // Already on dashboard
            navigateToDashboard();
            return;
        }

        // Check if we're already on this tab's activity
        boolean isCurrentActivity = false;
        switch (tabIndex) {
            case 0:
                isCurrentActivity = currentClassName.equals("MyClassesActivity");
                break;
            case 1:
                isCurrentActivity = currentClassName.equals("AttendanceSheetActivity");
                break;
            case 2:
                isCurrentActivity = currentClassName.equals("GradeInputActivity");
                break;
            case 3:
                isCurrentActivity = currentClassName.equals("UploadMaterialActivity");
                break;
        }

        if (isCurrentActivity) return; // Already on this tab's activity

        Intent intent;
        switch (tabIndex) {
            case 0:
                intent = new Intent(this, MyClassesActivity.class);
                break;
            case 1:
                intent = new Intent(this, AttendanceSheetActivity.class);
                break;
            case 2:
                intent = new Intent(this, GradeInputActivity.class);
                break;
            case 3:
                intent = new Intent(this, UploadMaterialActivity.class);
                break;
            default:
                return;
        }

        intent.putExtra("lecturer_id", lecturerId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);

        // Finish current activity unless it's the dashboard
        if (!currentClassName.equals("TeacherDashboardActivity")) {
            finish();
        }
    }

    private void highlightCurrentTab() {
        // Reset all tabs to inactive state
        resetAllTabs();

        String currentClassName = this.getClass().getSimpleName();

        // Determine current tab based on activity class
        if (currentClassName.equals("TeacherDashboardActivity")) {
            currentTabIndex = -1;
            setTabActive(iconDashboard, labelDashboard);
        } else if (currentClassName.equals("MyClassesActivity")) {
            currentTabIndex = 0;
            setTabActive(iconMyClasses, labelMyClasses);
        } else if (currentClassName.equals("AttendanceSheetActivity")) {
            currentTabIndex = 1;
            setTabActive(iconAttendance, labelAttendance);
        } else if (currentClassName.equals("GradeInputActivity")) {
            currentTabIndex = 2;
            setTabActive(iconGrade, labelGrade);
        } else if (currentClassName.equals("UploadMaterialActivity")) {
            currentTabIndex = 3;
            setTabActive(iconUpload, labelUpload);
        }
    }

    private void resetAllTabs() {
        int inactiveColor = ContextCompat.getColor(this, R.color.bottom_nav_unselected);

        if (iconDashboard != null) {
            iconDashboard.setColorFilter(inactiveColor);
            labelDashboard.setTextColor(inactiveColor);
        }
        if (iconMyClasses != null) {
            iconMyClasses.setColorFilter(inactiveColor);
            labelMyClasses.setTextColor(inactiveColor);
        }
        if (iconAttendance != null) {
            iconAttendance.setColorFilter(inactiveColor);
            labelAttendance.setTextColor(inactiveColor);
        }
        if (iconGrade != null) {
            iconGrade.setColorFilter(inactiveColor);
            labelGrade.setTextColor(inactiveColor);
        }
        if (iconUpload != null) {
            iconUpload.setColorFilter(inactiveColor);
            labelUpload.setTextColor(inactiveColor);
        }
    }

    private void setTabActive(ImageView icon, TextView label) {
        int activeColor = ContextCompat.getColor(this, R.color.bottom_nav_selected);
        if (icon != null && label != null) {
            icon.setColorFilter(activeColor);
            label.setTextColor(activeColor);
        }
    }

    protected void showBottomNavigation() {
        if (bottomNavigationContainer != null) {
            bottomNavigationContainer.setVisibility(View.VISIBLE);
        }
    }

    protected void hideBottomNavigation() {
        if (bottomNavigationContainer != null) {
            bottomNavigationContainer.setVisibility(View.GONE);
        }
    }

    // Method to navigate back to dashboard
    protected void navigateToDashboard() {
        Intent intent = new Intent(this, TeacherDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}

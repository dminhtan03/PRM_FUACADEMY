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
    private LinearLayout tabDashboard, tabMyClasses, tabCourse, tabSettings;
    private ImageView iconDashboard, iconMyClasses, iconCourse, iconSettings;
    private TextView labelDashboard, labelMyClasses, labelCourse, labelSettings;

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
        tabCourse = findViewById(R.id.tab_course);
        tabSettings = findViewById(R.id.tab_settings);

        iconDashboard = findViewById(R.id.icon_dashboard);
        iconMyClasses = findViewById(R.id.icon_my_classes);
        iconCourse = findViewById(R.id.icon_course);
        iconSettings = findViewById(R.id.icon_settings);

        labelDashboard = findViewById(R.id.label_dashboard);
        labelMyClasses = findViewById(R.id.label_my_classes);
        labelCourse = findViewById(R.id.label_course);
        labelSettings = findViewById(R.id.label_settings);

        // Set up click listeners
        setupBottomNavClickListeners();

        // Highlight current tab based on activity
        highlightCurrentTab();
    }

    private void setupBottomNavClickListeners() {
        if (tabDashboard != null) {
            tabDashboard.setOnClickListener(v -> navigateToTab(-1)); // Home (Dashboard)
        }
        if (tabMyClasses != null) {
            tabMyClasses.setOnClickListener(v -> navigateToTab(0)); // Class
        }
        if (tabCourse != null) {
            tabCourse.setOnClickListener(v -> navigateToTab(1)); // Course
        }
        if (tabSettings != null) {
            tabSettings.setOnClickListener(v -> navigateToTab(2)); // Setting
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
                isCurrentActivity = currentClassName.equals("TeacherCoursesActivity");
                break;
            case 2:
                isCurrentActivity = currentClassName.equals("SettingsActivity");
                break;
        }

        if (isCurrentActivity) return; // Already on this tab's activity

        Intent intent;
        switch (tabIndex) {
            case 0:
                intent = new Intent(this, MyClassesActivity.class);
                break;
            case 1:
                intent = new Intent(this, TeacherCoursesActivity.class);
                break;
            case 2:
                intent = new Intent(this, SettingsActivity.class);
                break;
            default:
                return;
        }

        intent.putExtra("lecturer_id", lecturerId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);

        // Always finish current activity when navigating to a different tab
        // This ensures proper navigation flow
        finish();
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
        } else if (currentClassName.equals("TeacherCoursesActivity")) {
            currentTabIndex = 1;
            setTabActive(iconCourse, labelCourse);
        } else if (currentClassName.equals("SettingsActivity")) {
            currentTabIndex = 2;
            setTabActive(iconSettings, labelSettings);
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
        if (iconCourse != null) {
            iconCourse.setColorFilter(inactiveColor);
            labelCourse.setTextColor(inactiveColor);
        }
        if (iconSettings != null) {
            iconSettings.setColorFilter(inactiveColor);
            labelSettings.setTextColor(inactiveColor);
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

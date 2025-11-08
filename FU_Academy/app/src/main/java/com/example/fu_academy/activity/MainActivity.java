package com.example.fu_academy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;

import com.example.fu_academy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends ComponentActivity {
    private BottomNavigationView bottomNavigationView;
    private int currentSelectedItem = -1; // No item selected initially
    private boolean isInitialLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_main);

            // Initialize default student ID if not exists
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            if (!prefs.contains("student_id")) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("student_id", 1);
                editor.putLong("user_id", 1);
                editor.apply();
            }

            // Initialize sample data
            com.example.fu_academy.database.DatabaseInitializer.initializeDatabase(this);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            
            if (bottomNavigationView == null) {
                return;
            }
            
            // Set up listener
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    try {
                        int itemId = item.getItemId();
                        
                        // Skip if this is the same item (prevent reloading)
                        if (itemId == currentSelectedItem) {
                            return true;
                        }
                        
                        // Skip initial load - don't auto-open screen on first load
                        if (isInitialLoad) {
                            isInitialLoad = false;
                            currentSelectedItem = itemId;
                            // Don't load screen, just update selection
                            return true;
                        }
                        
                        currentSelectedItem = itemId;
                        
                        // Load the corresponding screen
                        if (itemId == R.id.nav_dashboard) {
                            loadScreen(StudentDashboardActivity.class);
                            return true;
                        } else if (itemId == R.id.nav_schedule) {
                            // Show schedule options - default to weekly
                            loadScreen(WeeklyScheduleActivity.class);
                            return true;
                        } else if (itemId == R.id.nav_exam) {
                            loadScreen(ExamScheduleActivity.class);
                            return true;
                        } else if (itemId == R.id.nav_attendance) {
                            loadScreen(AttendanceDetailActivity.class);
                            return true;
                        } else if (itemId == R.id.nav_more) {
                            // Show more options dialog or navigate to a menu
                            // For now, show Academic Summary
                            loadScreen(AcademicSummaryActivity.class);
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            
            // Set default selection visually (this will trigger listener but we handle it with isInitialLoad flag)
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        } catch (Exception e) {
            e.printStackTrace();
            // Don't auto-load screen on error, just show MainActivity
        }
    }

    private void loadScreen(Class<?> activityClass) {
        try {
            Intent intent = new Intent(this, activityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore selection when returning to MainActivity
        if (bottomNavigationView != null) {
            try {
                bottomNavigationView.setSelectedItemId(currentSelectedItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        try {
            // If on dashboard, exit app; otherwise go back to dashboard
            if (currentSelectedItem == R.id.nav_dashboard) {
                super.onBackPressed();
            } else if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}


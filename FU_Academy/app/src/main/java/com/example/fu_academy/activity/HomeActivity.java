package com.example.fu_academy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.helper.SharedPreferencesHelper;
import com.example.fu_academy.activity.TeacherDashboardActivity;
import com.example.fu_academy.viewmodel.UserViewModel;
import com.example.fu_academy.viewmodel.StudentDashboardViewModel;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView txtWelcome;
    private UserViewModel userViewModel;
    private StudentDashboardViewModel dashboardViewModel;
    private SharedPreferencesHelper prefsHelper;
    private User currentUser;
    
    // Dashboard views
    private TextView tvGPA, tvTotalCredits, tvSemester, tvNotificationCount;
    private TextView tvUpcomingClass, tvAttendanceRate, tvRank, tvFeedbackCount;
    private Spinner spinnerMenu;
    private View dashboardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        txtWelcome = findViewById(R.id.txtWelcome);

        prefsHelper = new SharedPreferencesHelper(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        dashboardViewModel = new ViewModelProvider(this).get(StudentDashboardViewModel.class);
        
        // Đảm bảo database được khởi tạo
        com.example.fu_academy.database.EducationDatabase.getInstance(this);

        // Initialize dashboard views
        initDashboardViews();

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Setup drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup navigation view
        navigationView.setNavigationItemSelectedListener(this);

        // Load user info
        long userId = prefsHelper.getUserId();
        if (userId > 0) {
            userViewModel.getUserById(userId);
        }

        userViewModel.currentUser.observe(this, user -> {
            if (user != null) {
                currentUser = user;
                updateWelcomeMessage(user);
                updateNavigationHeader(user);
                
                // Redirect based on user role
                if ("student".equalsIgnoreCase(user.role)) {
                    if (dashboardContainer != null) {
                        dashboardContainer.setVisibility(View.VISIBLE);
                    }
                    loadDashboardData();
                } else if ("lecturer".equalsIgnoreCase(user.role)) {
                    // Redirect lecturer to Teacher Dashboard
                    Intent intent = new Intent(HomeActivity.this, TeacherDashboardActivity.class);
                    startActivity(intent);
//                    finish();
                } else {
                    // Hide dashboard for other roles
                    if (dashboardContainer != null) {
                        dashboardContainer.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    
    private void initDashboardViews() {
        tvGPA = findViewById(R.id.tvGPA);
        tvTotalCredits = findViewById(R.id.tvTotalCredits);
        tvSemester = findViewById(R.id.tvSemester);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        tvUpcomingClass = findViewById(R.id.tvUpcomingClass);
        tvAttendanceRate = findViewById(R.id.tvAttendanceRate);
        tvRank = findViewById(R.id.tvRank);
        tvFeedbackCount = findViewById(R.id.tvFeedbackCount);
        spinnerMenu = findViewById(R.id.spinnerMenu);
        dashboardContainer = findViewById(R.id.dashboardContainer);
        
        // Setup spinner menu
        setupSpinnerMenu();
        
        // Observe dashboard data
        observeDashboardData();
    }
    
    private void setupSpinnerMenu() {
        if (spinnerMenu == null) return;
        
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, menuItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapter);

        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstSelect = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelect) {
                    isFirstSelect = false;
                    return;
                }

                String selected = parent.getItemAtPosition(position).toString();

                switch (selected) {
                    case "Lịch học theo tuần":
                        startActivity(new Intent(HomeActivity.this, WeeklyScheduleActivity.class));
                        break;
                    case "Lịch học theo tháng":
                        startActivity(new Intent(HomeActivity.this, MonthlyCalendarActivity.class));
                        break;
                    case "Lịch thi":
                        startActivity(new Intent(HomeActivity.this, ExamScheduleActivity.class));
                        break;
                    case "Báo cáo điểm danh":
                        startActivity(new Intent(HomeActivity.this, AttendanceDetailActivity.class));
                        break;
                    case "Tổng kết học tập":
                        startActivity(new Intent(HomeActivity.this, AcademicSummaryActivity.class));
                        break;
                    case "Ý kiến đánh giá":
                        startActivity(new Intent(HomeActivity.this, FeedbackFormActivity.class));
                        break;
                    case "Khung chương trình":
                        startActivity(new Intent(HomeActivity.this, CourseListActivity.class));
                        break;
                    case "Điểm theo kỳ":
                        startActivity(new Intent(HomeActivity.this, GradePerSemesterActivity.class));
                        break;
                    case "Danh sách bài tập":
                        // Need course_id, for now open with default or show all
                        Intent assignmentIntent = new Intent(HomeActivity.this, AssignmentListActivity.class);
                        // You can pass course_id if available, or modify to show all assignments
                        assignmentIntent.putExtra("course_id", -1); // -1 means show all
                        startActivity(assignmentIntent);
                        break;
                    case "Tài liệu môn học":
                        Intent materialIntent = new Intent(HomeActivity.this, MaterialListActivity.class);
                        materialIntent.putExtra("course_id", -1); // -1 means show all
                        startActivity(materialIntent);
                        break;
                    case "Bài tập đã nộp":
                        startActivity(new Intent(HomeActivity.this, SubmissionListActivity.class));
                        break;
                }

                spinnerMenu.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    
    private void loadDashboardData() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long studentId = prefs.getLong("student_id", -1);
        
        // Nếu không có student_id trong SharedPreferences, thử lấy từ user_id
        if (studentId <= 0 && currentUser != null && "student".equalsIgnoreCase(currentUser.role)) {
            studentId = currentUser.user_id;
            // Lưu lại để dùng sau
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("student_id", studentId);
            editor.apply();
        }
        
        // Debug: Log studentId để kiểm tra
        android.util.Log.d("HomeActivity", "Loading dashboard data for studentId: " + studentId);
        
        if (studentId > 0 && dashboardViewModel != null) {
            dashboardViewModel.loadDashboardData(studentId);
        } else {
            android.util.Log.e("HomeActivity", "Cannot load dashboard: studentId=" + studentId + ", viewModel=" + (dashboardViewModel != null));
        }
    }
    
    private void observeDashboardData() {
        if (dashboardViewModel == null) return;
        
        dashboardViewModel.getGpa().observe(this, gpa -> {
            if (tvGPA != null && gpa != null) {
                tvGPA.setText(String.format("%.2f", gpa));
            }
        });

        dashboardViewModel.getTotalCredits().observe(this, credits -> {
            if (tvTotalCredits != null && credits != null) {
                tvTotalCredits.setText(String.valueOf(credits));
            }
        });

        dashboardViewModel.getSemester().observe(this, semester -> {
            if (tvSemester != null && semester != null) {
                tvSemester.setText(semester);
            }
        });

        dashboardViewModel.getNotificationCount().observe(this, count -> {
            if (tvNotificationCount != null && count != null) {
                tvNotificationCount.setText(String.valueOf(count));
            }
        });

        dashboardViewModel.getUpcomingClass().observe(this, schedule -> {
            if (tvUpcomingClass != null) {
                if (schedule != null) {
                    String classInfo = schedule.date + " " + schedule.time + " - " + schedule.room;
                    tvUpcomingClass.setText(classInfo);
                } else {
                    tvUpcomingClass.setText("Không có lớp sắp tới");
                }
            }
        });

        dashboardViewModel.getAttendanceRate().observe(this, rate -> {
            if (tvAttendanceRate != null && rate != null) {
                tvAttendanceRate.setText(String.format("%.1f%%", rate));
            }
        });

        dashboardViewModel.getRank().observe(this, rank -> {
            if (tvRank != null && rank != null) {
                tvRank.setText("#" + rank);
            }
        });

        dashboardViewModel.getFeedbackCount().observe(this, count -> {
            if (tvFeedbackCount != null && count != null) {
                tvFeedbackCount.setText(String.valueOf(count));
            }
        });
    }

    private void updateWelcomeMessage(User user) {
        String welcomeText = "Xin chào, " + (user.name != null ? user.name : "Người dùng") + "!";
        txtWelcome.setText(welcomeText);
    }

    private void updateNavigationHeader(User user) {
        View headerView = navigationView.getHeaderView(0);
        TextView txtHeaderName = headerView.findViewById(R.id.txtHeaderName);
        TextView txtHeaderEmail = headerView.findViewById(R.id.txtHeaderEmail);
        
        if (txtHeaderName != null) {
            txtHeaderName.setText(user.name != null ? user.name : "Người dùng");
        }
        if (txtHeaderEmail != null) {
            txtHeaderEmail.setText(user.email != null ? user.email : "");
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Already on home, just close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileOverviewActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_courses) {
            Intent intent = new Intent(this, CourseListActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_grade_per_semester) {
            Intent intent = new Intent(this, GradePerSemesterActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_assignment_list) {
            Intent intent = new Intent(this, AssignmentListActivity.class);
            intent.putExtra("course_id", -1);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_material_list) {
            Intent intent = new Intent(this, MaterialListActivity.class);
            intent.putExtra("course_id", -1);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_submission_list) {
            Intent intent = new Intent(this, SubmissionListActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_logout) {
            logout();
        }

        return true;
    }

    private void logout() {
        prefsHelper.logout();
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user info when returning to home
        long userId = prefsHelper.getUserId();
        if (userId > 0) {
            userViewModel.getUserById(userId);
        }
        
        // Reload dashboard data if user is student
        if (currentUser != null && "student".equalsIgnoreCase(currentUser.role)) {
            loadDashboardData();
        }
    }
}


package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.example.fu_academy.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView txtWelcome;
    private UserViewModel userViewModel;
    private SharedPreferencesHelper prefsHelper;
    private User currentUser;

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
    }
}


package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fu_academy.R;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.helper.SharedPreferencesHelper;

public class SettingsActivity extends BaseTeacherActivity {

    private TextView tvUserName, tvUserEmail, tvUserPhone, tvUserRole, tvUserMajor, tvUserAddress, tvUserGender;
    private Button btnLogout, btnEditProfile;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserEmail = findViewById(R.id.tv_user_email);
        tvUserPhone = findViewById(R.id.tv_user_phone);
        tvUserRole = findViewById(R.id.tv_user_role);
        tvUserMajor = findViewById(R.id.tv_user_major);
        tvUserAddress = findViewById(R.id.tv_user_address);
        tvUserGender = findViewById(R.id.tv_user_gender);
        btnLogout = findViewById(R.id.btn_logout);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
    }

    private void loadUserData() {
        try {
            EducationDatabase db = EducationDatabase.getInstance(this);
            currentUser = db.userDao().getUserById(lecturerId);

            if (currentUser != null) {
                updateUI();
            } else {
                Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateUI() {
        if (currentUser == null) return;

        tvUserName.setText(currentUser.name != null ? currentUser.name : "N/A");
        tvUserEmail.setText(currentUser.email != null ? currentUser.email : "N/A");
        tvUserPhone.setText(currentUser.phone != null ? currentUser.phone : "N/A");
        tvUserRole.setText(currentUser.role != null ? capitalizeFirst(currentUser.role) : "N/A");
        tvUserMajor.setText(currentUser.major != null ? currentUser.major : "N/A");
        tvUserAddress.setText(currentUser.address != null ? currentUser.address : "N/A");
        tvUserGender.setText(currentUser.gender != null ? currentUser.gender : "N/A");
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());

        btnEditProfile.setOnClickListener(v -> {
            // Navigate to EditProfileActivity
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("user_id", lecturerId);
            startActivity(intent);
        });
    }

    private void showLogoutConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> performLogout())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void performLogout() {
        try {
            // Clear shared preferences
            SharedPreferencesHelper prefs = new SharedPreferencesHelper(this);
            prefs.logout();

            // Navigate to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đăng xuất: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data when returning to this activity
        loadUserData();
    }
}

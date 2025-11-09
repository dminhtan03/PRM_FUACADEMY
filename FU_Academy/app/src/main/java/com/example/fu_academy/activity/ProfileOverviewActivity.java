package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.helper.SharedPreferencesHelper;
import com.example.fu_academy.viewmodel.UserViewModel;

public class ProfileOverviewActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private TextView txtName, txtID, txtEmail, txtRole, txtPhone, txtMajor, txtLastLogin;
    private ImageView imgAvatar;
    private Button btnEdit;
    private SharedPreferencesHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_overview);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Hồ Sơ");
        }

        txtName = findViewById(R.id.txtName);
        txtID = findViewById(R.id.txtID);
        txtEmail = findViewById(R.id.txtEmail);
        txtRole = findViewById(R.id.txtRole);
        txtPhone = findViewById(R.id.txtPhone);
        txtMajor = findViewById(R.id.txtMajor);
        txtLastLogin = findViewById(R.id.txtLastLogin);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnEdit = findViewById(R.id.btnEdit);

        prefsHelper = new SharedPreferencesHelper(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        long userId = prefsHelper.getUserId();
        if (userId > 0) {
            userViewModel.getUserById(userId);
        }

        userViewModel.currentUser.observe(this, user -> {
            if (user != null) {
                displayUserInfo(user);
            }
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });
    }

    private void displayUserInfo(User user) {
        txtName.setText("Tên: " + (user.name != null ? user.name : "N/A"));
        txtID.setText("ID: " + user.user_id);
        txtEmail.setText("Email: " + (user.email != null ? user.email : "N/A"));
        txtRole.setText("Vai trò: " + (user.role != null ? user.role : "N/A"));
        txtPhone.setText("Số điện thoại: " + (user.phone != null ? user.phone : "N/A"));
        txtMajor.setText("Chuyên ngành: " + (user.major != null ? user.major : "N/A"));
        txtLastLogin.setText("Đăng nhập lần cuối: " + (user.lastLogin != null ? user.lastLogin : "N/A"));

        // Load avatar if available
        if (user.avatar != null && !user.avatar.isEmpty()) {
            // In production, use image loading library like Glide or Picasso
            // imgAvatar.setImageURI(Uri.parse(user.avatar));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long userId = prefsHelper.getUserId();
        if (userId > 0) {
            userViewModel.getUserById(userId);
        }
    }
}


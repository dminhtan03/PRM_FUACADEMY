package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.helper.SharedPreferencesHelper;
import com.example.fu_academy.viewmodel.UserViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText edtName, edtPhone, edtAddress, edtEmail, edtMajor;
    private Spinner spinnerGender;
    private ImageView imgAvatar;
    private Button btnSave;
    private SharedPreferencesHelper prefsHelper;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Chỉnh Sửa Hồ Sơ");
        }

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtEmail = findViewById(R.id.edtEmail);
        edtMajor = findViewById(R.id.edtMajor);
        spinnerGender = findViewById(R.id.spinnerGender);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnSave = findViewById(R.id.btnSave);

        // Setup gender spinner
        String[] genders = {"Nam", "Nữ", "Khác"};
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        prefsHelper = new SharedPreferencesHelper(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        long userId = prefsHelper.getUserId();
        if (userId > 0) {
            userViewModel.getUserById(userId);
        }

        userViewModel.currentUser.observe(this, user -> {
            if (user != null) {
                currentUser = user;
                loadUserData(user);
            }
        });

        imgAvatar.setOnClickListener(v -> {
            // In production, implement image picker
            Toast.makeText(this, "Chức năng chọn ảnh đại diện sẽ được thêm sau", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            if (currentUser != null) {
                saveUserData();
            }
        });

        userViewModel.updateProfileResult.observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadUserData(User user) {
        edtName.setText(user.name);
        edtPhone.setText(user.phone);
        edtAddress.setText(user.address);
        edtEmail.setText(user.email);
        edtMajor.setText(user.major);

        if (user.gender != null) {
            String[] genders = {"Nam", "Nữ", "Khác"};
            for (int i = 0; i < genders.length; i++) {
                if (genders[i].equals(user.gender)) {
                    spinnerGender.setSelection(i);
                    break;
                }
            }
        }

        if (user.avatar != null && !user.avatar.isEmpty()) {
            // Load avatar image
        }
    }

    private void saveUserData() {
        currentUser.name = edtName.getText().toString().trim();
        currentUser.phone = edtPhone.getText().toString().trim();
        currentUser.address = edtAddress.getText().toString().trim();
        currentUser.email = edtEmail.getText().toString().trim();
        currentUser.major = edtMajor.getText().toString().trim();
        currentUser.gender = spinnerGender.getSelectedItem().toString();

        if (currentUser.name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.updateProfile(currentUser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


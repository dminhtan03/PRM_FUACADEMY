package com.example.fu_academy.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.helper.SharedPreferencesHelper;
import com.example.fu_academy.repository.UserRepository;
import com.example.fu_academy.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChangePasswordActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText edtOldPass, edtNewPass, edtConfirm;
    private Button btnChange;
    private TextView txtValidation, txtResult, txtTime, txtMsg;
    private String email;
    private SharedPreferencesHelper prefsHelper;
    private boolean isForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Đổi Mật Khẩu");
        }

        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirm = findViewById(R.id.edtConfirm);
        btnChange = findViewById(R.id.btnChange);
        txtValidation = findViewById(R.id.txtValidation);
        txtResult = findViewById(R.id.txtResult);
        txtTime = findViewById(R.id.txtTime);
        txtMsg = findViewById(R.id.txtMsg);

        prefsHelper = new SharedPreferencesHelper(this);
        email = getIntent().getStringExtra("email");
        isForgotPassword = email != null && getIntent().getBooleanExtra("forgot_password", false);
        
        if (email == null) {
            email = prefsHelper.getEmail();
        }

        // Hide old password field if it's from forgot password flow
        if (isForgotPassword) {
            edtOldPass.setVisibility(View.GONE);
            findViewById(R.id.txtOldPassLabel).setVisibility(View.GONE);
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Real-time validation
        edtNewPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validatePassword();
            }
        });

        edtConfirm.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateConfirmPassword();
            }
        });

        btnChange.setOnClickListener(v -> {
            String oldPass = edtOldPass.getText().toString().trim();
            String newPass = edtNewPass.getText().toString().trim();
            String confirm = edtConfirm.getText().toString().trim();

            if (!isForgotPassword && oldPass.isEmpty()) {
                txtMsg.setText("Vui lòng nhập mật khẩu cũ");
                txtMsg.setVisibility(View.VISIBLE);
                return;
            }

            if (newPass.isEmpty() || confirm.isEmpty()) {
                txtMsg.setText("Vui lòng nhập đầy đủ thông tin");
                txtMsg.setVisibility(View.VISIBLE);
                return;
            }

            if (!validatePassword() || !validateConfirmPassword()) {
                return;
            }

            if (isForgotPassword) {
                // For forgot password, directly update without old password check
                UserRepository repository = new UserRepository(this);
                repository.updatePassword(email, newPass);
                txtResult.setText("Đổi mật khẩu thành công");
                txtResult.setVisibility(View.VISIBLE);
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                txtTime.setText("Thời gian: " + currentTime);
                txtTime.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                userViewModel.changePassword(email, oldPass, newPass);
            }
        });

        userViewModel.changePasswordResult.observe(this, result -> {
            if (result != null) {
                txtResult.setText(result);
                txtResult.setVisibility(View.VISIBLE);
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                txtTime.setText("Thời gian: " + currentTime);
                txtTime.setVisibility(View.VISIBLE);

                if (result.contains("thành công")) {
                    txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    txtResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    txtMsg.setText(result);
                    txtMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean validatePassword() {
        String password = edtNewPass.getText().toString().trim();
        if (password.length() < 6) {
            txtValidation.setText("Mật khẩu phải có ít nhất 6 ký tự");
            txtValidation.setVisibility(View.VISIBLE);
            return false;
        }
        txtValidation.setVisibility(View.GONE);
        return true;
    }

    private boolean validateConfirmPassword() {
        String newPass = edtNewPass.getText().toString().trim();
        String confirm = edtConfirm.getText().toString().trim();
        if (!TextUtils.equals(newPass, confirm)) {
            txtValidation.setText("Mật khẩu xác nhận không khớp");
            txtValidation.setVisibility(View.VISIBLE);
            return false;
        }
        if (txtValidation.getVisibility() == View.VISIBLE && txtValidation.getText().toString().contains("khớp")) {
            txtValidation.setVisibility(View.GONE);
        }
        return true;
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


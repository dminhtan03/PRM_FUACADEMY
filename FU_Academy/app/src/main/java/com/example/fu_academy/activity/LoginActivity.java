package com.example.fu_academy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fu_academy.R;
import com.example.fu_academy.helper.SharedPreferencesHelper;
import com.example.fu_academy.viewmodel.UserViewModel;

public class LoginActivity extends ComponentActivity {
    private UserViewModel userViewModel;
    private EditText edtEmail, edtPassword;
    private Spinner spinnerRole;
    private CheckBox chkRememberMe;
    private Button btnLogin;
    private TextView txtErrorMsg;
    private ProgressBar progressLoading;
    private SharedPreferencesHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        chkRememberMe = findViewById(R.id.chkRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        txtErrorMsg = findViewById(R.id.txtErrorMsg);
        progressLoading = findViewById(R.id.progressLoading);

        // Setup role spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        prefsHelper = new SharedPreferencesHelper(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Load saved credentials if RememberMe was checked
        if (prefsHelper.isRememberMe()) {
            edtEmail.setText(prefsHelper.getEmail());
            edtPassword.setText(prefsHelper.getPassword());
            chkRememberMe.setChecked(true);
        }

        // Observe login result
        userViewModel.loggedUser.observe(this, user -> {
            if (user != null) {
                // Save user info to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("user_id", user.user_id);
                editor.putLong("student_id", user.user_id); // Assuming user_id is student_id for students
                editor.putString("user_name", user.name);
                editor.putString("user_role", user.role);
                editor.apply();

                prefsHelper.saveLoginInfo(edtEmail.getText().toString().trim(),
                        edtPassword.getText().toString().trim(),
                        user.role,
                        chkRememberMe.isChecked());
                prefsHelper.saveUserId(user.user_id);
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        userViewModel.loginError.observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                txtErrorMsg.setText(error);
                txtErrorMsg.setVisibility(View.VISIBLE);
            }
        });

        userViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading != null) {
                progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                btnLogin.setEnabled(!isLoading);
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            txtErrorMsg.setVisibility(View.GONE);

            if (email.isEmpty() || password.isEmpty()) {
                txtErrorMsg.setText("Vui lòng nhập đầy đủ thông tin");
                txtErrorMsg.setVisibility(View.VISIBLE);
            } else {
                userViewModel.loginWithRole(email, password, role);
            }
        });

        // Forgot password link
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        if (txtForgotPassword != null) {
            txtForgotPassword.setOnClickListener(v -> {
                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);
            });
        }
    }
}

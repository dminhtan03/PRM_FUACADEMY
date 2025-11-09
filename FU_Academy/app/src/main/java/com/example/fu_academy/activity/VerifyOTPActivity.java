package com.example.fu_academy.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.fu_academy.viewmodel.OTPViewModel;

public class VerifyOTPActivity extends AppCompatActivity {
    private OTPViewModel otpViewModel;
    private EditText edtEmail, edtOTP;
    private Button btnVerify;
    private TextView txtMsg;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Xác Thực OTP");
        }

        edtEmail = findViewById(R.id.edtEmail);
        edtOTP = findViewById(R.id.edtOTP);
        btnVerify = findViewById(R.id.btnVerify);
        txtMsg = findViewById(R.id.txtMsg);

        email = getIntent().getStringExtra("email");
        if (email != null) {
            edtEmail.setText(email);
            edtEmail.setEnabled(false);
        }

        otpViewModel = new ViewModelProvider(this).get(OTPViewModel.class);

        btnVerify.setOnClickListener(v -> {
            String emailValue = edtEmail.getText().toString().trim();
            String code = edtOTP.getText().toString().trim();

            if (emailValue.isEmpty() || code.isEmpty()) {
                txtMsg.setText("Vui lòng nhập đầy đủ thông tin");
                txtMsg.setVisibility(View.VISIBLE);
                return;
            }

            otpViewModel.verifyOTP(emailValue, code);
        });

        otpViewModel.otpVerified.observe(this, verified -> {
            if (verified != null) {
                if (verified) {
                    Toast.makeText(this, "Xác thực OTP thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("email", edtEmail.getText().toString().trim());
                    intent.putExtra("forgot_password", true);
                    startActivity(intent);
                    finish();
                } else {
                    txtMsg.setText(otpViewModel.otpMessage.getValue());
                    txtMsg.setVisibility(View.VISIBLE);
                }
            }
        });
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


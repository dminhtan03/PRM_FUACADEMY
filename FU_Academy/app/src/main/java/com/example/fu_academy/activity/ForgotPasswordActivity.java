package com.example.fu_academy.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
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

public class ForgotPasswordActivity extends AppCompatActivity {
    private OTPViewModel otpViewModel;
    private EditText edtEmail, edtOTP;
    private Button btnSend, btnConfirm;
    private TextView txtTimer, txtStatus, txtMsg, txtCode;
    private CountDownTimer countDownTimer;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Quên Mật Khẩu");
        }

        edtEmail = findViewById(R.id.edtEmail);
        edtOTP = findViewById(R.id.edtOTP);
        btnSend = findViewById(R.id.btnSend);
        btnConfirm = findViewById(R.id.btnConfirm);
        txtTimer = findViewById(R.id.txtTimer);
        txtStatus = findViewById(R.id.txtStatus);
        txtMsg = findViewById(R.id.txtMsg);
        txtCode = findViewById(R.id.txtCode);

        otpViewModel = new ViewModelProvider(this).get(OTPViewModel.class);

        btnSend.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                txtMsg.setText("Vui lòng nhập email");
                txtMsg.setVisibility(View.VISIBLE);
                return;
            }
            currentEmail = email;
            otpViewModel.sendOTP(email);
            btnSend.setEnabled(false);
            startTimer();
        });

        otpViewModel.otpStatus.observe(this, status -> {
            if (status != null) {
                switch (status) {
                    case "success":
                        txtStatus.setText("Đã gửi");
                        txtStatus.setVisibility(View.VISIBLE);
                        txtMsg.setText(otpViewModel.otpMessage.getValue());
                        txtMsg.setVisibility(View.VISIBLE);
                        edtOTP.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        // For demo purposes, show OTP code
                        otpViewModel.otpCode.observe(this, code -> {
                            if (code != null) {
                                txtCode.setText("Mã OTP: " + code);
                                txtCode.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case "error":
                        txtStatus.setText("Lỗi");
                        txtStatus.setVisibility(View.VISIBLE);
                        txtMsg.setText(otpViewModel.otpMessage.getValue());
                        txtMsg.setVisibility(View.VISIBLE);
                        btnSend.setEnabled(true);
                        break;
                }
            }
        });

        btnConfirm.setOnClickListener(v -> {
            String code = edtOTP.getText().toString().trim();
            if (code.isEmpty()) {
                txtMsg.setText("Vui lòng nhập mã OTP");
                txtMsg.setVisibility(View.VISIBLE);
                return;
            }
            otpViewModel.verifyOTP(currentEmail, code);
        });

        otpViewModel.otpVerified.observe(this, verified -> {
            if (verified != null) {
                if (verified) {
                    Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    // Navigate to change password
                    android.content.Intent intent = new android.content.Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("email", currentEmail);
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

    private void startTimer() {
        countDownTimer = new CountDownTimer(300000, 1000) { // 5 minutes
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                txtTimer.setText(String.format("Thời gian còn lại: %02d:%02d", minutes, seconds));
                txtTimer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                txtTimer.setText("Mã OTP đã hết hạn");
                btnSend.setEnabled(true);
            }
        }.start();
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
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}


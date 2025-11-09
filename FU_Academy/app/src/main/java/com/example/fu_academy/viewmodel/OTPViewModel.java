package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.entity.OTP;
import com.example.fu_academy.repository.OTPRepository;

import java.util.Random;

public class OTPViewModel extends AndroidViewModel {
    private final OTPRepository repository;
    public MutableLiveData<String> otpStatus = new MutableLiveData<>();
    public MutableLiveData<String> otpMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> otpVerified = new MutableLiveData<>();
    public MutableLiveData<String> otpCode = new MutableLiveData<>();

    public OTPViewModel(Application application) {
        super(application);
        repository = new OTPRepository(application);
    }

    public void sendOTP(String email) {
        // Generate 6-digit OTP
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1000000));
        
        long currentTime = System.currentTimeMillis();
        long expiresAt = currentTime + (5 * 60 * 1000); // 5 minutes

        OTP otp = new OTP(email, code, currentTime, expiresAt);
        long otpId = repository.insertOTP(otp);

        if (otpId > 0) {
            otpStatus.postValue("success");
            otpMessage.postValue("Mã OTP đã được gửi đến email của bạn");
            otpCode.postValue(code); // In production, this should be sent via email/SMS
        } else {
            otpStatus.postValue("error");
            otpMessage.postValue("Không thể gửi mã OTP. Vui lòng thử lại");
        }
    }

    public void verifyOTP(String email, String code) {
        OTP otp = repository.getValidOTP(email, code);
        if (otp != null) {
            repository.markAsUsed(otp.otp_id);
            otpVerified.postValue(true);
            otpStatus.postValue("verified");
            otpMessage.postValue("Xác thực OTP thành công");
        } else {
            otpVerified.postValue(false);
            otpStatus.postValue("invalid");
            otpMessage.postValue("Mã OTP không hợp lệ hoặc đã hết hạn");
        }
    }

    public OTP getLatestOTP(String email) {
        return repository.getLatestOTP(email);
    }
}


package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.entity.OTP;
import com.example.fu_academy.helper.EmailSender;
import com.example.fu_academy.repository.UserRepository;
import com.example.fu_academy.repository.OTPRepository;

import java.util.Random;

public class OTPViewModel extends AndroidViewModel {
    private final OTPRepository repository;
    private final UserRepository userRepository;
    public MutableLiveData<String> otpStatus = new MutableLiveData<>();
    public MutableLiveData<String> otpMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> otpVerified = new MutableLiveData<>();
    // Không hiển thị OTP ra màn hình nữa

    public OTPViewModel(Application application) {
        super(application);
        repository = new OTPRepository(application);
        userRepository = new UserRepository(application);
    }

    public void sendOTP(String email) {
        // Check email exists
        com.example.fu_academy.entity.User user = userRepository.getUserByEmail(email);
        if (user == null) {
            otpStatus.postValue("error");
            otpMessage.postValue("Email không tồn tại trong hệ thống");
            return;
        }

        // Generate 6-digit OTP
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1000000));
        
        long currentTime = System.currentTimeMillis();
        long expiresAt = currentTime + (3 * 60 * 1000); // 3 minutes

        OTP otp = new OTP(email, code, currentTime, expiresAt);
        long otpId = repository.insertOTP(otp);

        if (otpId <= 0) {
            otpStatus.postValue("error");
            otpMessage.postValue("Không thể tạo mã OTP. Vui lòng thử lại");
            return;
        }

        // Đánh dấu thành công ngay để chuyển màn Verify OTP
        otpStatus.postValue("success");
        otpMessage.postValue("Đã gửi OTP tới email của bạn");

        // Gửi email trên luồng nền (best-effort, không chặn luồng)
        new Thread(() -> {
            boolean sent = EmailSender.sendOtpEmail(email, code);
            if (!sent) {
                // Không đổi trạng thái để không phá vỡ luồng; chỉ ghi log nếu cần
                android.util.Log.w("OTPViewModel", "Gửi email OTP thất bại. Kiểm tra cấu hình SMTP.");
            }
        }).start();
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


package com.example.fu_academy.repository;

import android.content.Context;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.OTP;

public class OTPRepository {
    private final EducationDatabase db;

    public OTPRepository(Context context) {
        db = EducationDatabase.getInstance(context);
    }

    public long insertOTP(OTP otp) {
        return db.otpDao().insertOTP(otp);
    }

    public OTP getValidOTP(String email, String code) {
        long currentTime = System.currentTimeMillis();
        return db.otpDao().getValidOTP(email, code, currentTime);
    }

    public OTP getLatestOTP(String email) {
        long currentTime = System.currentTimeMillis();
        return db.otpDao().getLatestOTP(email, currentTime);
    }

    public void markAsUsed(long otpId) {
        db.otpDao().markAsUsed(otpId);
    }
}


package com.example.fu_academy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fu_academy.entity.OTP;

@Dao
public interface OTPDao {
    @Insert
    long insertOTP(OTP otp);

    @Update
    void updateOTP(OTP otp);

    @Query("SELECT * FROM OTP WHERE email = :email AND code = :code AND isUsed = 0 AND expiresAt > :currentTime ORDER BY createdAt DESC LIMIT 1")
    OTP getValidOTP(String email, String code, long currentTime);

    @Query("SELECT * FROM OTP WHERE email = :email AND isUsed = 0 AND expiresAt > :currentTime ORDER BY createdAt DESC LIMIT 1")
    OTP getLatestOTP(String email, long currentTime);

    @Query("UPDATE OTP SET isUsed = 1 WHERE otp_id = :otpId")
    void markAsUsed(long otpId);
}


package com.example.fu_academy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "OTP")
public class OTP {
    @PrimaryKey(autoGenerate = true)
    public long otp_id;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "code")
    public String code;

    @ColumnInfo(name = "createdAt")
    public long createdAt;

    @ColumnInfo(name = "expiresAt")
    public long expiresAt;

    @ColumnInfo(name = "isUsed")
    public boolean isUsed;

    public OTP() {
    }

    public OTP(String email, String code, long createdAt, long expiresAt) {
        this.email = email;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isUsed = false;
    }

    public long getOtp_id() {
        return otp_id;
    }

    public void setOtp_id(long otp_id) {
        this.otp_id = otp_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}


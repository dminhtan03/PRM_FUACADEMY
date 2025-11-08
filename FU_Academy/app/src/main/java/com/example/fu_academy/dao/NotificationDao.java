package com.example.fu_academy.dao;

import androidx.room.*;

import com.example.fu_academy.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    long insert(Notification n);

    @Query("SELECT * FROM Notification ORDER BY date DESC")
    List<Notification> getAll();

    @Query("SELECT * FROM Notification WHERE role_target = :role")
    List<Notification> getByRole(String role);
}
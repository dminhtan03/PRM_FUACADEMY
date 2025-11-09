package com.example.fu_academy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.fu_academy.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE user_id = :id")
    User getUserById(long id);

    @Query("SELECT * FROM User WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM User WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM User WHERE email = :email AND password = :password AND role = :role LIMIT 1")
    User loginWithRole(String email, String password, String role);

    @Query("UPDATE User SET password = :newPassword WHERE email = :email")
    void updatePassword(String email, String newPassword);

    @Query("UPDATE User SET lastLogin = :lastLogin WHERE user_id = :userId")
    void updateLastLogin(long userId, String lastLogin);

    @Query("SELECT * FROM User WHERE role LIKE :roleFilter AND email LIKE :emailFilter AND status LIKE :statusFilter")
    List<User> getUsersWithFilter(String roleFilter, String emailFilter, String statusFilter);
}
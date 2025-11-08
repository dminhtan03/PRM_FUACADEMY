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
}
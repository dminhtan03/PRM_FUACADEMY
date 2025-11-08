package com.example.fu_academy.repository;

import android.content.Context;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.User;

import java.util.List;

public class UserRepository {
    private final EducationDatabase db;

    public UserRepository(Context context) {
        db = EducationDatabase.getInstance(context);
    }

    public void insertUser(User user) {
        db.userDao().insertUser(user);
    }

    public User login(String email, String password) {
        return db.userDao().login(email, password);
    }
    public List<User> getAllUsers() {
        return db.userDao().getAllUsers();
    }
}
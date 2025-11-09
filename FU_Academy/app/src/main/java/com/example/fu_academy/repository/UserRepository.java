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

    public User loginWithRole(String email, String password, String role) {
        return db.userDao().loginWithRole(email, password, role);
    }

    public User getUserByEmail(String email) {
        return db.userDao().getUserByEmail(email);
    }

    public User getUserById(long id) {
        return db.userDao().getUserById(id);
    }

    public void updateUser(User user) {
        db.userDao().updateUser(user);
    }

    public void updatePassword(String email, String newPassword) {
        db.userDao().updatePassword(email, newPassword);
    }

    public void updateLastLogin(long userId, String lastLogin) {
        db.userDao().updateLastLogin(userId, lastLogin);
    }

    public List<User> getAllUsers() {
        return db.userDao().getAllUsers();
    }

    public List<User> getUsersWithFilter(String roleFilter, String emailFilter, String statusFilter) {
        return db.userDao().getUsersWithFilter(roleFilter, emailFilter, statusFilter);
    }
}
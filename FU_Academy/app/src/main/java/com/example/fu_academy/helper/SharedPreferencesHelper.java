package com.example.fu_academy.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "FU_Academy_Prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ROLE = "role";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveLoginInfo(String email, String password, String role, boolean rememberMe) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        if (rememberMe) {
            editor.putString(KEY_PASSWORD, password);
        } else {
            editor.remove(KEY_PASSWORD);
        }
        editor.apply();
    }

    public void saveUserId(long userId) {
        editor.putLong(KEY_USER_ID, userId);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return prefs.getString(KEY_PASSWORD, "");
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "");
    }

    public boolean isRememberMe() {
        return prefs.getBoolean(KEY_REMEMBER_ME, false);
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearLoginInfo() {
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_ROLE);
        editor.remove(KEY_REMEMBER_ME);
        editor.remove(KEY_USER_ID);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    public void logout() {
        if (!isRememberMe()) {
            clearLoginInfo();
        } else {
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.remove(KEY_USER_ID);
            editor.apply();
        }
    }

    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}


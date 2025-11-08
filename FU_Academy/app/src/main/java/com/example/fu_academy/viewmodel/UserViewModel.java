package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.entity.User;
import com.example.fu_academy.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;
    public MutableLiveData<User> loggedUser = new MutableLiveData<>();
    public MutableLiveData<String> loginError = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<String> changePasswordResult = new MutableLiveData<>();
    public MutableLiveData<User> currentUser = new MutableLiveData<>();
    public MutableLiveData<Boolean> updateProfileResult = new MutableLiveData<>();
    public MutableLiveData<List<User>> userList = new MutableLiveData<>();

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void login(String email, String password) {
        isLoading.postValue(true);
        User user = repository.login(email, password);
        isLoading.postValue(false);
        if (user != null) {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            repository.updateLastLogin(user.user_id, currentTime);
            user.lastLogin = currentTime;
            loggedUser.postValue(user);
        } else {
            loginError.postValue("Email hoặc mật khẩu không đúng");
        }
    }

    public void loginWithRole(String email, String password, String role) {
        isLoading.postValue(true);
        User user = repository.loginWithRole(email, password, role);
        isLoading.postValue(false);
        if (user != null) {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            repository.updateLastLogin(user.user_id, currentTime);
            user.lastLogin = currentTime;
            loggedUser.postValue(user);
        } else {
            loginError.postValue("Email, mật khẩu hoặc vai trò không đúng");
        }
    }

    public void getUserById(long userId) {
        User user = repository.getUserById(userId);
        currentUser.postValue(user);
    }

    public void getUserByEmail(String email) {
        User user = repository.getUserByEmail(email);
        currentUser.postValue(user);
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = repository.getUserByEmail(email);
        if (user == null) {
            changePasswordResult.postValue("Người dùng không tồn tại");
            return;
        }
        if (!user.password.equals(oldPassword)) {
            changePasswordResult.postValue("Mật khẩu cũ không đúng");
            return;
        }
        repository.updatePassword(email, newPassword);
        changePasswordResult.postValue("Đổi mật khẩu thành công");
    }

    public void updateProfile(User user) {
        repository.updateUser(user);
        updateProfileResult.postValue(true);
        currentUser.postValue(user);
    }

    public void getAllUsers() {
        List<User> users = repository.getAllUsers();
        userList.postValue(users);
    }

    public void getUsersWithFilter(String roleFilter, String emailFilter, String statusFilter) {
        String role = roleFilter.isEmpty() ? "%" : "%" + roleFilter + "%";
        String email = emailFilter.isEmpty() ? "%" : "%" + emailFilter + "%";
        String status = statusFilter.isEmpty() ? "%" : "%" + statusFilter + "%";
        List<User> users = repository.getUsersWithFilter(role, email, status);
        userList.postValue(users);
    }

    public void updateUserRole(long userId, String newRole) {
        User user = repository.getUserById(userId);
        if (user != null) {
            user.role = newRole;
            repository.updateUser(user);
            getAllUsers();
        }
    }

    public void updateUserStatus(long userId, String newStatus) {
        User user = repository.getUserById(userId);
        if (user != null) {
            user.status = newStatus;
            repository.updateUser(user);
            getAllUsers();
        }
    }
}
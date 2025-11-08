package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.entity.User;
import com.example.fu_academy.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;
    public MutableLiveData<User> loggedUser = new MutableLiveData<>();
    public MutableLiveData<String> loginError = new MutableLiveData<>();

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void login(String email, String password) {
        User user = repository.login(email, password);
        if (user != null) {
            loggedUser.postValue(user);
        } else {
            loginError.postValue("Email hoặc mật khẩu không đúng");
        }
    }

    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }
}
package com.example.moneyless.ui.add;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moneyless.data.Entity.User;
import com.example.moneyless.data.UserRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<List<User>> getAllUserLive() {
        return userRepository.getAllUsersLive();
    }

    public void insertUser(User... users) {
        userRepository.insertUser(users);
    }
}

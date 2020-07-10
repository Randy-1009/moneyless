package com.example.moneyless.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moneyless.data.CategoryRepository;
import com.example.moneyless.data.Entity.Category;
import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.data.Entity.User;
import com.example.moneyless.data.UserRepository;
import com.example.moneyless.data.WasteBookRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private WasteBookRepository wasteBookRepository;
    private CategoryRepository categoryRepository;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        wasteBookRepository = new  WasteBookRepository(application);
        categoryRepository = new CategoryRepository(application);
    }

    public LiveData<List<User>> getAllUserLive() {
        return userRepository.getAllUsersLive();
    }

    public void insertUser(User... users) {
        userRepository.insertUser(users);
    }
    public void insertWasteBook(MoneyLess... moneyLesses) {
        wasteBookRepository.insertWasteBook(moneyLesses);
    }
    public void insertCategory(Category... categories) {
        categoryRepository.insertCategory(categories);
    }
}

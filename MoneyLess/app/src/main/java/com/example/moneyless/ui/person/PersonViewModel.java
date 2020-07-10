package com.example.moneyless.ui.person;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.moneyless.data.CategoryRepository;
import com.example.moneyless.data.Entity.Category;
import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.data.Entity.User;
import com.example.moneyless.data.UserRepository;
import com.example.moneyless.data.WasteBookRepository;

import java.util.ArrayList;
import java.util.List;

public class PersonViewModel extends AndroidViewModel {
    private WasteBookRepository wasteBookRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    public PersonViewModel(@NonNull Application application) {
        super(application);
        wasteBookRepository = new WasteBookRepository(application);
        categoryRepository = new CategoryRepository(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<List<MoneyLess>> getAllWasteBookLive() {
        return wasteBookRepository.getAllWasteBooksLive();
    }

    public LiveData<List<Category>> getAllCategoriesLive() {
        return categoryRepository.getAllCategoriesLive();
    }
    public LiveData<List<MoneyLess>> getAllWasteBookUpload() {
        return wasteBookRepository.getAllWasteBooksUpload();
    }
    public void deleteUser() {
        userRepository.deleteUser();
    }
    public void deleteCategoty(Category...categories) {
        categoryRepository.deleteCategory(categories);
    }

    public void updateWasteBook(MoneyLess... moneyless) {
        wasteBookRepository.updateWasteBook(moneyless);
    }

    public void updateCategory(Category... categories) {
        categoryRepository.updateCategory(categories);
    }

    public void insertWasteBook(MoneyLess... moneyless) {
        wasteBookRepository.insertWasteBook(moneyless);
    }

    public void deleteAllWasteBooks() {
        wasteBookRepository.deleteAllWasteBooks();
    }
}
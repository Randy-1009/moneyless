package com.example.moneyless.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.data.WasteBookRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private WasteBookRepository wasteBookRepository;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        wasteBookRepository = new WasteBookRepository(application);
    }


    public LiveData<List<MoneyLess>> getAllWasteBookLive(){
        return wasteBookRepository.getAllWasteBooksLive();
    }
}

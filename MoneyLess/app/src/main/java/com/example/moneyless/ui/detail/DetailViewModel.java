package com.example.moneyless.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.data.WasteBookRepository;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {

    private WasteBookRepository wasteBookRepository;
    public DetailViewModel(@NonNull Application application) {
        super(application);
        wasteBookRepository = new WasteBookRepository(application);
    }
    public LiveData<List<MoneyLess>> getAllWasteBookLive(){
        return wasteBookRepository.getAllWasteBooksLive();
    }
    public LiveData<List<MoneyLess>> findWasteBookWithPattern(String pattern){
        return wasteBookRepository.findWasteBookWithPattern(pattern);
    }

    public void insertWasteBook(MoneyLess... moneyless) {
        wasteBookRepository.insertWasteBook(moneyless);
    }

    public void updateWasteBook(MoneyLess... moneyless){
        wasteBookRepository.updateWasteBook(moneyless);
    }
    public void deleteWasteBook(MoneyLess... moneyless){
        wasteBookRepository.deleteWasteBook(moneyless);
    }

//    public LiveData<List<WasteBook>> selectWasteBookByLongTime(long a,long b){
//        return wasteBookRepository.selectWasteBookByLongTime(a,b);
//    }

}
package com.example.moneyless.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.moneyless.data.Entity.MoneyLess;

import java.util.List;

public class WasteBookRepository {
    private LiveData<List<MoneyLess>> allWasteBooksLive;
    private WasteBookDao wasteBookDao;

   public  WasteBookRepository(Context context){
        WasteBookDatabase wasteBookDatabase = WasteBookDatabase.getDatabase(context.getApplicationContext());
        wasteBookDao = wasteBookDatabase.getWasteBookDao();
        allWasteBooksLive = wasteBookDao.getAllWasteBookLive();
    }

    public void insertWasteBook(MoneyLess...moneyless){
        new InsertAsyncTask(wasteBookDao).execute(moneyless);
    }

    public void updateWasteBook(MoneyLess...moneyless){
        new UpdateAsyncTask(wasteBookDao).execute(moneyless);
    }
    public void deleteWasteBook(MoneyLess...moneyless){
        new DeleteAsyncTask(wasteBookDao).execute(moneyless);
    }
    public void deleteAllWasteBooks(){
        new DeleteAllAsyncTask(wasteBookDao).execute();
    }

//    public LiveData<List<WasteBook>> selectWasteBookByLongTime(long a,long b){
//       return wasteBookDao.selectWasteBookByLongTime(a,b);
//    }

    public LiveData<List<MoneyLess>>findWasteBookWithPattern(String pattern){
        return wasteBookDao.findWordsWithPattern("%" + pattern + "%");
    }
    public LiveData<List<MoneyLess>> getAllWasteBooksLive(){
        return allWasteBooksLive;
    }
    public LiveData<List<MoneyLess>> getAllWasteBooksLiveByAmount(){
        return wasteBookDao.getAllWasteBookLiveByAmount();
    }
    public LiveData<List<MoneyLess>> getAllWasteBooksUpload(){
        return wasteBookDao.getAllWasteBookUpload();
    }

    private static class InsertAsyncTask extends AsyncTask<MoneyLess,Void,Void> {
        private WasteBookDao wasteBookDao;
        public InsertAsyncTask(WasteBookDao wasteBookDao) {
            this.wasteBookDao = wasteBookDao;
        }

        @Override
        protected Void doInBackground(MoneyLess... moneyless) {
            wasteBookDao.insertWasteBook(moneyless);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<MoneyLess,Void,Void> {
        private WasteBookDao wasteBookDao;
        public UpdateAsyncTask(WasteBookDao wasteBookDao) {
            this.wasteBookDao = wasteBookDao;
        }

        @Override
        protected Void doInBackground(MoneyLess... wasteBooks) {
            wasteBookDao.updateWasteBook(wasteBooks);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<MoneyLess,Void,Void> {
        private WasteBookDao wasteBookDao;
        public DeleteAsyncTask(WasteBookDao wasteBookDao) {
            this.wasteBookDao = wasteBookDao;
        }

        @Override
        protected Void doInBackground(MoneyLess... moneyless) {
            wasteBookDao.deleteWasteBook(moneyless);
            return null;
        }
    }
    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private WasteBookDao wasteBookDao;
        public DeleteAllAsyncTask(WasteBookDao wasteBookDao) {
            this.wasteBookDao = wasteBookDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            wasteBookDao.deleteAllWasteBooks();
            return null;
        }
    }

}

package com.example.moneyless.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneyless.data.Entity.MoneyLess;

import java.util.List;

@Dao
public interface WasteBookDao {
    @Insert
    void insertWasteBook(MoneyLess... moneyless);

    @Update
    void updateWasteBook(MoneyLess... moneyless);

    //删除单条记录
    @Delete
    void deleteWasteBook(MoneyLess... moneyless);

    @Query("DELETE FROM MoneyLess")
    void deleteAllWasteBooks();

    //获取所有的记录按时间排序
    @Query("SELECT * FROM MoneyLess ORDER BY create_datetime DESC")
    LiveData<List<MoneyLess>> getAllWasteBookLive();
    //获取所有未同步的记录按时间排序
    @Query("SELECT * FROM MoneyLess WHERE UPLOAD = 0 ORDER BY create_datetime DESC ")
    LiveData<List<MoneyLess>> getAllWasteBookUpload();
    //获取所有的记录按金额大小排序
    @Query("SELECT * FROM MoneyLess ORDER BY amount DESC")
    LiveData<List<MoneyLess>> getAllWasteBookLiveByAmount();
    //搜索数据库，范围包括备注和类型
    @Query("SELECT * FROM MoneyLess WHERE note LIKE :pattern or category LIKE :pattern ORDER BY ID DESC")
    LiveData<List<MoneyLess>> findWordsWithPattern(String pattern);

//    @Query("SELECT * FROM WasteBook WHERE create_datetime between :a and :b")
//    //@Query("select * from WasteBook where time between datetime(date(datetime('now',strftime('-%w day','now'))),' 1 second') and datetime(date(datetime('now',(6 - strftime('%w day','now'))||' day','1 day')),'-1 second')")
//    LiveData<List<WasteBook>> selectWasteBookByLongTime(long a,long b);
}

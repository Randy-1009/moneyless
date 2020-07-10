package com.example.moneyless.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneyless.data.Entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Category...categories);

    @Update
    void updateCategory(Category...categories);

    @Delete
    void deleteCategory(Category...categories);

    @Query("DELETE FROM CATEGORY")
    void deleteAllCategory();

    @Query("SELECT * FROM CATEGORY ORDER BY category_order")
    LiveData<List<Category>>getAllCategoriesLive();

}

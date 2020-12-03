package com.anlyn.alonevirtue.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface FavorObjectDao  {

    @Insert
    void insert(FavoriteObjectItem item);
    @Update
    void update(FavoriteObjectItem item);
    @Delete
    void delete(FavoriteObjectItem item);

    @Query("DELETE FROM FavorItem_table")
    void deleteAll();

    @Query("SELECT * FROM FavorItem_table")
    LiveData<List<FavoriteObjectItem>> getList();
}

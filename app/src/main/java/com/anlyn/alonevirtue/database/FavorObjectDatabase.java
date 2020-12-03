package com.anlyn.alonevirtue.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
//https://stackoverflow.com/questions/57473172/android-room-one-database-with-multiple-tables
@Database(entities = {FavoriteObjectItem.class},version = 1)
abstract class FavorObjectDatabase extends RoomDatabase {

    private static FavorObjectDatabase instance;

    abstract FavorObjectDao favorDao();

    public synchronized FavorObjectDatabase getInstance(Context context){
        if(instance == null){
            instance= Room.databaseBuilder(context.getApplicationContext(),FavorObjectDatabase.class,"FavorItemDB").fallbackToDestructiveMigration()
                    .build();
            //do something
        }
        return instance;
    }
}

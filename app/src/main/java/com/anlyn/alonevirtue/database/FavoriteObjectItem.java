package com.anlyn.alonevirtue.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavorItem_table")
public class FavoriteObjectItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String pathName;

    public FavoriteObjectItem(int id, String name, String pathName) {
        this.id = id;
        this.name = name;
        this.pathName = pathName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPathName() {
        return pathName;
    }

}

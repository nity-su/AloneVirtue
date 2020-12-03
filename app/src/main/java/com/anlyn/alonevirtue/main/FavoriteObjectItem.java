package com.anlyn.alonevirtue.main;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class FavoriteObjectItem {

    private Bitmap bitmap;
    private String name;
    private String pathName;

    public FavoriteObjectItem(String pathName, String name) {
        this.pathName = pathName;
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}

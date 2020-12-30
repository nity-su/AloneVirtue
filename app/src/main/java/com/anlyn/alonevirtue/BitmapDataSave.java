package com.anlyn.alonevirtue;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapDataSave {

    public void makeBitmapFileOutput(Bitmap bitmap, String name){


        //Bitmap을 Byte Array로 전환
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //주목
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , bos);
        byte[] bitmapdata = bos.toByteArray();
        Log.d("byte",bitmapdata.length+"");
        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos =new FileOutputStream(new File(name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.anlyn.alonevirtue;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class ReduceBitmap {
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) { // 이미지 너비 높이 한계치 설정
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

         while ((halfHeight / inSampleSize) > reqHeight&& (halfWidth / inSampleSize) > reqWidth) {
             inSampleSize *= 2;
         }
        }
        return inSampleSize;
    }

    public static BitmapFactory.Options getOptionSampleSize(InputStream stream, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream,null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        return options;
    }

    public static BitmapFactory.Options getOptionSampleSize(Bitmap bitmap, int reqWidth, int reqHeight){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //주목
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , bos);
        byte[] bitmapdata = bos.toByteArray();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapdata,0,bitmapdata.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        return options;
    }


    public static Bitmap decodedBitmapFromStream(InputStream stream,BitmapFactory.Options options) {
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(stream,null, options);
    }

    public static Bitmap decodedBitmapFromStream(Bitmap bitmap,BitmapFactory.Options options) {
        // First decode with inJustDecodeBounds=true to check dimensions
        // Calculate inSampleSize
        //options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);     // Decode bitmap with inSampleSize set
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //주목
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , bos);
        byte[] bitmapdata = bos.toByteArray();
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bitmapdata,0,bitmapdata.length, options);
    }

    public static String getRealPathFromURI(Uri contentUri, Context context)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null); //Since manageQuery is deprecated
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}

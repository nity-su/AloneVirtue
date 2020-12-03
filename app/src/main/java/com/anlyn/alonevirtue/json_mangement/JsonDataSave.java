package com.anlyn.alonevirtue.json_mangement;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonDataSave {

    public boolean create(Context context, String fileName, String jsonString){
        try {
//            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            FileOutputStream fos =new FileOutputStream(new File(fileName));
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }


}

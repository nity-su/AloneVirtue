package com.anlyn.alonevirtue.main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.json_mangement.JsonDataSave;
import com.anlyn.alonevirtue.json_mangement.JsonTransHandler;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

class MainAppCompatActivity extends AppCompatActivity implements BottomMainDialogFragment.BottomMainListener {
    @Override
    public void deleteListener(Context context, final String folderPath, final String imagePath, final int position, final MutableLiveData<List<FavoriteObjectItem>> liveData, final String basePath) {
        new AsyncTask<Void,Void,List<FavoriteObjectItem>>(){
    @Override
    protected List<FavoriteObjectItem> doInBackground(Void... voids) {
        List<FavoriteObjectItem> list = liveData.getValue();
        list.remove(position);
        File folder =new File(folderPath);
        if(folder.isDirectory()) {
            deleteRecursive(folder);
        }
        Log.d("url",folder.toString());
        File image = new File(imagePath);
        if(image.exists())
            image.delete();

        FavoriteObjectJsonHandler handler =new FavoriteObjectJsonHandler();
        handler.setList(list);
        JSONObject jsonObject = (JSONObject)handler.encode(JsonTransHandler.OBJECT_TO_JSON);
        //create json file
        JsonDataSave jsonDataSave =new JsonDataSave();
        String path = basePath+File.separator+"FavoriteObject";
        jsonDataSave.create(getApplicationContext(),path,jsonObject.toString());

        return list;
    }

            @Override
            protected void onPostExecute(List<FavoriteObjectItem> list) {
               liveData.setValue(list);
            }
        }.execute();

    }
    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}

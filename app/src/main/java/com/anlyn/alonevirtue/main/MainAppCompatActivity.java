package com.anlyn.alonevirtue.main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.contents.ui.video.VideoContInfoJsonHandler;
import com.anlyn.alonevirtue.contents.ui.video.VideoContentsInfo;
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

    @Override
    public void changePosToTop(Context context,int position ,MutableLiveData<List<FavoriteObjectItem>> liveData, String basePath) {
        new AsyncTask<Void, Void, List<FavoriteObjectItem>>() {
            ProgressBarDialog dialog;
            @Override
            protected void onPreExecute() {
                dialog = new ProgressBarDialog();
            }
            @Override
            protected List<FavoriteObjectItem> doInBackground(Void... voids) {
                List<FavoriteObjectItem> objectItems = liveData.getValue();
                int choicePos = objectItems.size()-1;

                if(choicePos==-1)
                    return null;

                FavoriteObjectItem object = objectItems.remove(position);
                objectItems.add(0,object);
                String fileName =basePath+File.separator+"FavoriteObject" ;
                FavoriteObjectJsonHandler handler = new FavoriteObjectJsonHandler();
                handler.setList(objectItems);
                JSONObject jsonObject = (JSONObject) handler.encode(FavoriteObjectJsonHandler.OBJECT_TO_JSON);
                JsonDataSave jsonDataSave = new JsonDataSave();
                jsonDataSave.create(context,fileName,jsonObject.toString());

                return objectItems;
            }

            @Override
            protected void onPostExecute(List<FavoriteObjectItem> list) {
                if(list==null)
                    return;
                liveData.setValue(list);
                dialog.dismiss();
            }
        }.execute();
    }
}

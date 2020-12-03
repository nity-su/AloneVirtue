package com.anlyn.alonevirtue.main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anlyn.alonevirtue.json_mangement.JsonDataRead;
import com.anlyn.alonevirtue.json_mangement.JsonTransHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewModelMainActivity extends ViewModel {
    private MutableLiveData<List<FavoriteObjectItem>> liveData;
    public ViewModelMainActivity(){
        liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<FavoriteObjectItem>());
    }

    void populateData(final Context context,final String favorite_object_path) {
        new AsyncTask<Void,Void,List<FavoriteObjectItem>>(){
            @Override
            protected List<FavoriteObjectItem> doInBackground(Void... voids) {
                List<FavoriteObjectItem> list = new ArrayList<>();
                JsonDataRead jsonDataRead = new JsonDataRead();
                try {
                    FavoriteObjectJsonHandler handler;
                    String path = favorite_object_path+ File.separator+"FavoriteObject";
                    String result = jsonDataRead.read(context, path);
                    handler = new FavoriteObjectJsonHandler();

                    try {
                        handler.setJsonObject(new JSONObject(result));
                        list=(ArrayList<FavoriteObjectItem>)handler.decode(JsonTransHandler.JSON_TO_LIST);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }catch (NullPointerException e){

                }
                return list;
            }

            @Override
            protected void onPostExecute(List<FavoriteObjectItem> list) {
                Log.d("size",list.size()+"");
                liveData.setValue(list);
            }
        }.execute();

    }

    public MutableLiveData<List<FavoriteObjectItem>> getLiveData() {
        return liveData;
    }
}

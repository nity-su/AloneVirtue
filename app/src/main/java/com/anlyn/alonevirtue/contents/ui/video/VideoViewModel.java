package com.anlyn.alonevirtue.contents.ui.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.anlyn.alonevirtue.json_mangement.JsonDataRead;
import com.anlyn.alonevirtue.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoViewModel extends ViewModel {
    private final int REQUET_CODE = 1001;
    private MutableLiveData<String> mText;
    private MutableLiveData<AlertDialog.Builder> builder;
    private List<VideoContentsInfo> value;
    private MutableLiveData<List<VideoContentsInfo>> infoList;
    private ContentsAdapter adapter;
    private String path;
    private boolean selected;
    public VideoViewModel(final Fragment fragment , String path) {

        this.path=path;
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        final String items[] =fragment.getResources().getStringArray(R.array.content_type);
        final String[] result = new String[1];
        selected=false;
        builder = new MutableLiveData<>();
        builder.setValue(new AlertDialog.Builder(fragment.getActivity()).setTitle("불러오기").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result[0] =items[which];
                selected=true;
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!selected)
                    return;
                selected=false;
                Intent intent = new Intent(fragment.getActivity(),AddVideoInfoActivity.class);
                intent.putExtra("video_type",result[0]);
                fragment.startActivityForResult(intent,REQUET_CODE);
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));
        init(fragment.getActivity());
    }

    void init(Context context){
        populateData(context);
        infoList = new MutableLiveData<>();
        infoList.setValue(value);

    }
    void populateData(Context context){
        JsonDataRead jsonDataRead = new JsonDataRead();
        String jsonStr = null;
        try {
            jsonStr = jsonDataRead.read(context, path);
        }catch (NullPointerException e){

        }

        if(jsonStr ==null) {
            value =new ArrayList<>();
        return ;
        }

        VideoContInfoJsonHandler handler=new VideoContInfoJsonHandler();
        try {
            handler.setJsonObject(new JSONObject(jsonStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        value=new ArrayList<>();
        value=(ArrayList<VideoContentsInfo>)handler.decode(VideoContInfoJsonHandler.JSON_TO_LIST);

    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<AlertDialog.Builder> getDialog(){return builder;}
    public MutableLiveData<List<VideoContentsInfo>> getMutableLiveDataInfoList(){
        return infoList;
    }

    public List<VideoContentsInfo> getValue() {
        return value;
    }

    public ContentsAdapter getContentsAdapter() {
       return adapter;
    }

}

 class VideoViewModelFatory implements ViewModelProvider.Factory{
     Fragment fragment;
    String path;
     VideoViewModelFatory(Fragment fragment,String path){
         this.fragment=fragment;
         this.path=path;
     }

     @NonNull
     @Override
     public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
         return (T) new VideoViewModel(fragment,path);
     }
 }
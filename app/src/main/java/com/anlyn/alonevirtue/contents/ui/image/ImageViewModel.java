package com.anlyn.alonevirtue.contents.ui.image;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.contents.ui.video.AddVideoInfoActivity;
import com.anlyn.alonevirtue.json_mangement.JsonDataRead;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImageViewModel extends ViewModel {
    private List<ImageInfo> imageInfolist;
    private MutableLiveData<List<ImageInfo>> listMutableLiveData;
    private MutableLiveData<AlertDialog.Builder> builder;
    private final int REQUET_CODE=1001;
    private boolean selected;
    public ImageViewModel(Fragment fragment,String path) {
        populateData(fragment.getContext(),path);
        initBuilder(fragment);
        listMutableLiveData=new MutableLiveData<>();
        listMutableLiveData.setValue(imageInfolist);
        selected=false;
        //setListMutableLiveData(imageInfolist);
    }

    void initBuilder(final Fragment fragment){
        builder=new MutableLiveData<>();
        final String[] result = new String[1];
        final String[] items = fragment.getResources().getStringArray(R.array.content_type);
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
                Intent intent = new Intent(fragment.getActivity(), AddImageActivity.class);
                intent.putExtra("image_type",result[0]);
                fragment.startActivityForResult(intent,REQUET_CODE);
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }));
    }

    public MutableLiveData<AlertDialog.Builder> getBuilder() {
        return builder;
    }

    public List<ImageInfo> getImageInfolist() {
        return imageInfolist;
    }

    public void setList(List<ImageInfo> list) {
        this.imageInfolist = list;
    }

    public void setListMutableLiveData(List<ImageInfo> list) {
        listMutableLiveData.setValue(list);
    }

    public MutableLiveData<List<ImageInfo>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void populateData(Context context,String path){
        imageInfolist=new ArrayList<>();
        JsonDataRead jsonData =new JsonDataRead();
        String toJson="";
        try {
            toJson = jsonData.read(context, path);
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        if(toJson=="")
            return;

        JSONObject jsonObject=null;
        try {
            jsonObject =new JSONObject(toJson);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            return;
        }

        ImageInfoJsonHandler handler =new ImageInfoJsonHandler();
        handler.setJsonObject(jsonObject);
        imageInfolist = (List<ImageInfo>)handler.decode(ImageInfoJsonHandler.JSON_TO_LIST);
    }

}

class ImageViewModelFatory implements ViewModelProvider.Factory{
    Fragment fragment;
    String path;
    ImageViewModelFatory(Fragment fragment,String path){
        this.fragment=fragment;
        this.path=path;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ImageViewModel(fragment,path);
    }
}


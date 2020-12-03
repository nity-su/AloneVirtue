package com.anlyn.alonevirtue.contents.ui.video;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.json_mangement.JsonDataSave;

import org.json.JSONObject;

import java.io.File;
import java.util.List;


public class VideoTouchControlListener implements View.OnClickListener,View.OnLongClickListener {
    String TAG="ㅈㄹ";
    private String url;
    private Context context;
    private String basePath;
    private MutableLiveData<List<VideoContentsInfo>> list;
    private int position;
    public VideoTouchControlListener(MutableLiveData<List<VideoContentsInfo>> list,String url, Context context,int position, String basePath) {
        this.url = url;
        this.context = context;
        this.basePath = basePath;
        this.list=list;
        this.position = position;
    }
        //https://software.intel.com/content/www/us/en/develop/blogs/video-playing-with-android-media-player.html
    @Override
    public void onClick(View v) {
        List<VideoContentsInfo> info = list.getValue();
        String type = info.get(position).getUriType();
        if(type.equals("NETWORK")) {
            Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            videoIntent.setPackage("com.android.chrome");
            videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(videoIntent);
        }else if(type.equals("GALLERY")){
            Intent videoIntent = new Intent(Intent.ACTION_VIEW);
            videoIntent.setDataAndType(Uri.parse(url),"video/*");
            videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(videoIntent);
        }
        }



    @Override
    public boolean onLongClick(View v) {
        v.setAlpha(0.5f);
        FragmentActivity fa = (FragmentActivity) v.getContext();
        bottomDialog(fa,v);
        return false;
    }

    void bottomDialog(FragmentActivity fa,View v){

        BottomVideoDialogFragment fragment = new BottomVideoDialogFragment(url,context,basePath,list,position,v);
        fragment.show(fa.getSupportFragmentManager(),"bottomDialog");
    }



}

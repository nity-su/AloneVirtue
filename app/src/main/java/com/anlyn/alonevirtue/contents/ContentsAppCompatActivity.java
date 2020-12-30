package com.anlyn.alonevirtue.contents;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.contents.ui.image.BottomImageDialogFragment;
import com.anlyn.alonevirtue.contents.ui.image.ImageInfo;
import com.anlyn.alonevirtue.contents.ui.image.ImageInfoJsonHandler;
import com.anlyn.alonevirtue.contents.ui.video.BottomVideoDialogFragment;
import com.anlyn.alonevirtue.contents.ui.video.ProgressTaskVideo;
import com.anlyn.alonevirtue.contents.ui.video.VideoContInfoJsonHandler;
import com.anlyn.alonevirtue.contents.ui.video.VideoContentsInfo;
import com.anlyn.alonevirtue.json_mangement.JsonDataSave;
import com.anlyn.alonevirtue.main.ProgressBarDialog;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class ContentsAppCompatActivity extends AppCompatActivity implements BottomImageDialogFragment.BottomItemListener, BottomVideoDialogFragment.BottomItemListener, ProgressTaskVideo.TitleAuto{

    @Override
    public void imageOnclickListener(final MutableLiveData<List<ImageInfo>> liveData,final String url,final Context context,final String baseUrl) {
        new AsyncTask<Void, Void, List<ImageInfo>>() {
            ProgressBarDialog dialog;
            @Override
            protected void onPreExecute() {
                dialog = new ProgressBarDialog();
                dialog.show();
            }

            @Override
            protected List<ImageInfo> doInBackground(Void... voids) {
                List<ImageInfo> list = liveData.getValue();
                Log.d("size",list.size()+"");
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getUrl().equals(url)){
                        Log.d("size",list.size()+"");
                        list.remove(i);
                        i--;
                        continue;
                    }
                }
                ImageInfoJsonHandler handler =new ImageInfoJsonHandler();
                handler.setList(list);
                JSONObject jsonObject=(JSONObject) handler.encode(ImageInfoJsonHandler.OBJECT_TO_JSON);
                JsonDataSave save =new JsonDataSave();
                save.create(context,baseUrl,jsonObject.toString());
                return list;
            }

            @Override
            protected void onPostExecute(List<ImageInfo> imageInfos) {
                liveData.setValue(imageInfos);
                dialog.dismiss();
            }
        }.execute();

    }

    @Override
    public void originalImageOpenListener(String url, Context context) {
        String pickUrl = url;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(pickUrl));
        context.startActivity(intent);
    }

    @Override
    public void deleteListener(final MutableLiveData<List<VideoContentsInfo>> list, final Context context, final String url, final String basePath) {
        new AsyncTask<Void, Void, List<VideoContentsInfo>>() {
            ProgressBarDialog dialog;
            @Override
            protected void onPreExecute() {
                dialog=new ProgressBarDialog();
                dialog.show();
            }

            @Override
            protected List<VideoContentsInfo> doInBackground(Void... voids) {
                List<VideoContentsInfo> infoList =list.getValue();
                final String filePath = url.replace("/", "").replace("\\", "");
                for(int i=0;i<infoList.size();i++){
                    if(infoList.get(i).getUrl().equals(url)){
                        infoList.remove(i);
                        i--;
                        new File(basePath+File.separator+filePath).delete();
                    }
                }

                String videoInfoFileName =basePath+File.separator+"VideoInfo" ;
                VideoContInfoJsonHandler handler = new VideoContInfoJsonHandler();
                handler.setList(infoList);
                JSONObject jsonObject = (JSONObject) handler.encode(VideoContInfoJsonHandler.OBJECT_TO_JSON);
                JsonDataSave save = new JsonDataSave();
                save.create(context,videoInfoFileName,jsonObject.toString());

                return infoList;
            }

            @Override
            protected void onPostExecute(List<VideoContentsInfo> infoList) {
                list.setValue(infoList);
                dialog.dismiss();
            }
        }.execute();

    }
    @Override
    public void changePosToTop(final MutableLiveData<List<VideoContentsInfo>> list,final Context context,final int position,final String basePath){

        new AsyncTask<Void, Void, List<VideoContentsInfo>>() {
            ProgressBarDialog dialog;
            @Override
            protected void onPreExecute() {
                dialog = new ProgressBarDialog();
            }
            @Override
            protected List<VideoContentsInfo> doInBackground(Void... voids) {
                List<VideoContentsInfo> videoInfo = list.getValue();
                int choicePos = videoInfo.size()-1;
                VideoContentsInfo object = videoInfo.remove(position);
                videoInfo.add(0,object);
                String videoInfoFileName =basePath+File.separator+"VideoInfo" ;
                VideoContInfoJsonHandler handler = new VideoContInfoJsonHandler();
                handler.setList(videoInfo);
                JSONObject jsonObject = (JSONObject) handler.encode(VideoContInfoJsonHandler.OBJECT_TO_JSON);
                JsonDataSave save = new JsonDataSave();
                save.create(context,videoInfoFileName,jsonObject.toString());

                return videoInfo;
            }

            @Override
            protected void onPostExecute(List<VideoContentsInfo> imageInfos) {

                list.setValue(imageInfos);
                dialog.dismiss();
            }
        }.execute();

    }
    private boolean isChecked;

    @Override
    public void setResult(boolean result) {
        isChecked =result;
    }

    @Override
    public boolean getResult() {
        return isChecked;
    }


}

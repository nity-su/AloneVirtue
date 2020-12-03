package com.anlyn.alonevirtue.contents.ui.video;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.json_mangement.JsonDataSave;
import com.anlyn.alonevirtue.main.ProgressBarDialog;
import com.anlyn.alonevirtue.R;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class VideoMenuClickListener implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private String url;
    private Context context;
    private String basePath;
    private MutableLiveData<List<VideoContentsInfo>> list;

    VideoMenuClickListener(String url, Context context , MutableLiveData<List<VideoContentsInfo>> list,String basePath){
        this.url=url;
        this.context = context;
        this.list=list;
        this.basePath=basePath;
    }
    @Override
    public void onClick(View v) {
        showPopupMenu(v);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //복사
        if(item.getItemId()==R.id.videoShare)
        clipBoardCopy();
        //삭제
        if(item.getItemId()==R.id.videoDelete) {
         new AsyncTask<String,String,Object>(){
             ProgressBarDialog dialog;
             @Override
             protected Object doInBackground(String...strings) {

                 return removeItem();
             }

             @Override
             protected void onPreExecute() {
                 dialog= new ProgressBarDialog();
                 dialog.show();
             }

             @Override
             protected void onPostExecute(Object object) {
                 List<VideoContentsInfo> infoList =(List<VideoContentsInfo>)object;
                 dialog.dismiss();
                 list.setValue(infoList);
             }
         }.execute();
//            removeItem();
        }

        return true;
    }

    void clipBoardCopy(){
        ClipData clipData;
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipData = ClipData.newPlainText("text",url);
        manager.setPrimaryClip(clipData);
    }
    List removeItem(){
        List<VideoContentsInfo> infoList =list.getValue();
        final String filePath = url.replace("/", "").replace("\\", "");
        for(int i=0;i<infoList.size();i++){
            if(infoList.get(i).getUrl().equals(url)){
                infoList.remove(i);
                i--;
                new File(basePath+File.separator+filePath).delete();
            }
        }
        save(infoList);
        return infoList;
    }
    void save(List<VideoContentsInfo> list){
        String videoInfoFileName =basePath+File.separator+"VideoInfo" ;
        VideoContInfoJsonHandler handler = new VideoContInfoJsonHandler();
        handler.setList(list);
        JSONObject jsonObject = (JSONObject) handler.encode(VideoContInfoJsonHandler.OBJECT_TO_JSON);
        JsonDataSave save = new JsonDataSave();
        save.create(context,videoInfoFileName,jsonObject.toString());
    }

    private void showPopupMenu(View view){
        PopupMenu popup = new PopupMenu(view.getContext(),view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.video_control_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }
}

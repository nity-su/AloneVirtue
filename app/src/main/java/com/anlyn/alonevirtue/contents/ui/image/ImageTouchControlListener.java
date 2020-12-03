package com.anlyn.alonevirtue.contents.ui.image;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.anlyn.alonevirtue.contents.ui.video.VideoContentsInfo;

import java.util.ArrayList;
import java.util.List;

public class ImageTouchControlListener implements View.OnLongClickListener,View.OnClickListener{
    private String baseUrl;
    private Long then;
    private int position;
    private MutableLiveData<List<ImageInfo>> liveData;
    private ImageTouchControlListener listener;
//    private Handler handler =new Handler(Looper.getMainLooper());
//    Runnable runnable;

    public ImageTouchControlListener(int position, MutableLiveData<List<ImageInfo>> liveData, String baseUrl) {
        this.position = position;
        this.liveData = liveData;
        listener = this;
        this.baseUrl = baseUrl;
    }


    @Override
    public void onClick(View v) {
        goToPhotoViewActivity(v);
    }
//https://www.androhub.com/android-popup-menu/
    @Override
    public boolean onLongClick(View v) {
        v.setAlpha(0.5f);
        bottomDialog(v);
        return true;
    }

    void goToPhotoViewActivity(View v){
        Context context = v.getContext();
        Intent intent = new Intent(context,PhotoViewActivity.class);
        intent.putParcelableArrayListExtra("imageUrl", (ArrayList) liveData.getValue());
//        intent.putStringArrayListExtra("imageUrl",(ArrayList<String>) url);
        intent.putExtra("position",position);
//        intent.putStringArrayListExtra()
        context.startActivity(intent);
    }
    private void originalPicture(Context context){
        String pickUrl = liveData.getValue().get(position).getUrl();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(pickUrl));
        context.startActivity(intent);

    }

    private void bottomDialog(View view){
        String pickUrl = liveData.getValue().get(position).getUrl();
        FragmentActivity context = (FragmentActivity)view.getContext();
        BottomImageDialogFragment dialog = new BottomImageDialogFragment(liveData,pickUrl,baseUrl);
        dialog.show(context.getSupportFragmentManager(),"bottomDialog");
    }
//https://androidwave.com/bottom-sheet-dialog-fragment-in-android/
    public ImageTouchControlListener getListener() {
        return listener;
    }

}

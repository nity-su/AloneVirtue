package com.anlyn.alonevirtue.contents.ui.video;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;

import com.anlyn.alonevirtue.BitmapDataSave;
import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.VideoContentsFrameMiniBinding;
import com.anlyn.alonevirtue.main.ProgressBarDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.List;

//https://stackoverflow.com/questions/45373007/progressdialog-is-deprecated-what-is-the-alternate-one-to-use
public class ProgressTaskVideo  extends AsyncTask<Integer, Integer, String[]> {
    private ImageView view;
    private String url;
    private String videoType;
    private String title;
    private String basePath;
    private Fragment fragment;
    private ProgressBarDialog dialog;
    private File file;
    private List<VideoContentsInfo> infoList;
    private String urlReplaced;
    private String saveFileName;
    private DataSave dataSave;
    private TitleAuto titleAuto;

    public interface TitleAuto{
        void setResult(boolean isChecked);
        boolean getResult();
    }

    public ProgressTaskVideo(ImageView view, VideoContentsInfo info, String basePath, Fragment fragment,List<VideoContentsInfo> infoList) {
        this.view = view;
        this.url = info.getUrl();
        this.videoType = info.getUriType();
        this.title=info.getTitle();
        this.basePath = basePath;
        this.fragment = fragment;
        this.infoList = infoList;
        this.titleAuto = (TitleAuto) view.getContext();
        dataSave = (DataSave)fragment;
    }


    @Override
    protected void onPreExecute() {
        urlReplaced = url.replace("//", "").replace("/", "");
        saveFileName = basePath + File.separator + urlReplaced;
        file = new File(saveFileName);

        if (!file.exists()) {
            dialog = new ProgressBarDialog();
            dialog.setProgressDialog(fragment.getActivity());
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(final String[] result) {
        final String urlPath = urlReplaced;
        final String finalPath = saveFileName;

        File file = new File(finalPath);

        if (!file.exists()) {
            String url = result[0];
            String title = result[1];
            ProgressBarDialog p = new ProgressBarDialog();
            p.setProgressDialog(fragment.getActivity());
//            VideoContentsFrameMiniBinding binding = DataBindingUtil.getBinding((View) view.getParent());
            //다른 접근 필요
            ViewDataBinding binding = DataBindingUtil.getBinding((View)view.getParent());
            binding.setVariable(BR.video_title,title);
            binding.executePendingBindings();

            Log.d("d", "cc");

            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_baseline_broken_image_24);
            options.error(R.drawable.ic_baseline_broken_image_24);
            options.fallback(R.drawable.ic_baseline_broken_image_24);

            Glide.with(fragment)
                    .setDefaultRequestOptions(options).asBitmap()
                    .load(url)
                    .transform(new CenterCrop(),new RoundedCorners(14))
                    .into(new CustomTarget<Bitmap>(256, 256) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    view.setImageBitmap(resource);
                    BitmapDataSave bd = new BitmapDataSave();
                    bd.makeBitmapFileOutput(resource, basePath + File.separator + urlPath);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            dialog.dismiss();
        } else {
            Log.d("d", "dd");
            Glide.with(fragment)
                    .load(finalPath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop(),new RoundedCorners(14))
                    .into(view);
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected String[] doInBackground(Integer... integers) {
//        File file = new File(saveFileName);
        String ogImageUrlresult="";
        String title="";
        if (!file.exists()) {
            VideoContentsInfo info;
            info = new OGTag().getOGTag(url, null);
            ogImageUrlresult = info.getUrl();
            if(titleAuto.getResult()) {
                title = info.getTitle();
            }else {
                title = this.title;
            }
            infoList.get(infoList.size()-1).setTitle(title);
            dataSave.save(basePath,infoList);
        }

    return new String[]{ogImageUrlresult,title};

    }

    public interface DataSave{
        void save(String videoInfoFileName,List<VideoContentsInfo> list);
    }


}


package com.anlyn.alonevirtue.contents.ui.video;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.anlyn.alonevirtue.BR;
import com.anlyn.alonevirtue.R;
import com.google.android.material.transition.Scale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

//https://stackoverflow.com/questions/44301545/2-way-databinding-for-webview-and-progressbar
public class ContentsAdapter  extends RecyclerView.Adapter<ContentsAdapter.Holder>{
    private List<VideoContentsInfo> list;
    private MutableLiveData<List<VideoContentsInfo>> liveData;
    private String basePath;
    private Context context;
    private Fragment fragment;
//    ContentsAdapter(List<VideoContentsInfo> info){
//        this.list=info;
//    }
    ContentsAdapter(String basePath,Context context,Fragment fragment){
        this.fragment = fragment;
        this.basePath = basePath;
        this.context = context;
        list=new ArrayList<>();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.video_contents_frame_mini,parent,false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String pickUrl = list.get(position).getUrl();
        VideoTouchControlListener listener =new VideoTouchControlListener(liveData,pickUrl,context,position,basePath);
//        holder.imageView.setOnClickListener(listener);
//        holder.imageView.setOnLongClickListener(listener);
        holder.hashTagViewBind(list.get(position).getTag());
        holder.bind(list.get(position),list.get(position).getTitle(),listener);
    }

    void setList(List<VideoContentsInfo> list){
//        this.list = list;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;
        ImageView imageView;
        public Holder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
//            imageViewResize(binding);
        }

        void imageViewResize(ViewDataBinding binding){

            Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics ();
            display.getMetrics(outMetrics);
            int width  = (int) outMetrics.widthPixels;
            int height = (int) outMetrics.heightPixels;
            float density = context.getResources().getDisplayMetrics().density;
            int dpWidth  = (int)((float)width/density);
            width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, context.getResources().getDisplayMetrics());
//            int dpHeight = (int)((float)height/density);
            int dpHeight = (int)(width*0.75f);
            imageView = binding.getRoot().findViewById(R.id.VideoThumnail);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width,dpHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }

        public void bind(Object obj,String title,VideoTouchControlListener listener){
            loadUrlBind(obj);
            titleTextBind(title);
            setListenerBind(listener);
            binding.executePendingBindings();
        }
        public void hashTagViewBind(List<String> tagList){
            binding.setVariable(BR.tagList,tagList);
        }
        public void loadUrlBind(Object obj){
            binding.setVariable(BR.video_info_obj,obj);
            binding.setVariable(BR.video_base_path,basePath);
            binding.setVariable(BR.videoList,list);
            binding.setVariable(BR.video_fragment,fragment);
        }
        public void titleTextBind(String title){
            binding.setVariable(BR.video_title,title);
        }
        public void setListenerBind(VideoTouchControlListener listener){
            binding.setVariable(BR.videoListener,listener);
        }

    }

    public MutableLiveData<List<VideoContentsInfo>> getLiveData() {
        return liveData;
    }

    public void setLiveData(MutableLiveData<List<VideoContentsInfo>> liveData) {
        this.liveData = liveData;
    }

    public void filter(String tag){
        List<VideoContentsInfo> listFiltered = new ArrayList<>();
        List<VideoContentsInfo> originList = liveData.getValue();
        Iterator iterator = originList.iterator();
        while (iterator.hasNext()){
            VideoContentsInfo info = (VideoContentsInfo)iterator.next();

            if(info.getTag()!=null && info.getTag().contains(tag)){
                listFiltered.add(info);
                continue;
            }
        }
        setList(listFiltered);
    }

    public void cancelFilter(){
        setList(liveData.getValue());
    }
}

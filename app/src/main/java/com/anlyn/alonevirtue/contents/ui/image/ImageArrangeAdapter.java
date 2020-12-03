package com.anlyn.alonevirtue.contents.ui.image;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anlyn.alonevirtue.BR;
import com.anlyn.alonevirtue.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
//https://www.dev2qa.com/android-recyclerview-example/
public class ImageArrangeAdapter extends RecyclerView.Adapter<ImageArrangeAdapter.Holder> {
    private MutableLiveData<List<ImageInfo>> liveData;
    private List<ImageInfo> infoList;
    private Fragment fragment;
    private String baseUrl;
    ImageArrangeAdapter(Fragment fragment,String baseUrl,MutableLiveData<List<ImageInfo>> liveData ){
        this.fragment=fragment;
        this.liveData = liveData;
        infoList=liveData.getValue();
        this.baseUrl=baseUrl;
        //infoList = list;
    }

    public void setList(List<ImageInfo> list) {
        this.infoList = list;
      notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.image_contents_frame,parent,false);
//        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.image_contents_frame,parent,false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
//        holder.imageView.setBackground(context.getDrawable(R.drawable.ic_launcher_background));
        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setFullSpan(true);
        ImageInfo info = infoList.get(position);
        holder.bind(info,position);
        Log.d("getUrl",infoList.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public ViewDataBinding binding;
        public Holder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bind(Object object,int position){
            binding.setVariable(BR.image_info_obj,object);
            binding.setVariable(BR.touch_listener,new ImageTouchControlListener(position,liveData,baseUrl));
            binding.setVariable(BR.fragment,fragment);
            binding.executePendingBindings();
        }
    }




}

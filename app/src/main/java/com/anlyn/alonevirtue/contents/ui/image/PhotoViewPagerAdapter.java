package com.anlyn.alonevirtue.contents.ui.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.anlyn.alonevirtue.BR;
import com.anlyn.alonevirtue.R;

import java.util.List;

class PhotoViewPagerAdapter extends RecyclerView.Adapter<PhotoViewPagerAdapter.ViewHoder> {
    List<ImageInfo> urlList;
    PhotoViewPagerAdapter(List<ImageInfo> list){
        urlList=list;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewDataBinding binding =  DataBindingUtil.inflate(inflater, R.layout.photo_view_cell,parent,false);
        return new ViewHoder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.bind(urlList.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    class ViewHoder extends RecyclerView.ViewHolder{
        ViewDataBinding binding;
        public ViewHoder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        void bind(Object obj){
            binding.setVariable(BR.photoViewUrl,obj);
            binding.executePendingBindings();
        }
    }
}

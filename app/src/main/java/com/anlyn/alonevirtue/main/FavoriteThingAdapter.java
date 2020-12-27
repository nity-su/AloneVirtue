package com.anlyn.alonevirtue.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.anlyn.alonevirtue.BR;
import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.contents.ContentsActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class FavoriteThingAdapter extends RecyclerView.Adapter<FavoriteThingAdapter.Holder> {
    private MutableLiveData<List<FavoriteObjectItem>> liveData;
    private List<FavoriteObjectItem> list;
    private Context context;
    private String favorite_object_path;
    public void setList(List<FavoriteObjectItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    FavoriteThingAdapter(MutableLiveData<List<FavoriteObjectItem>> liveData, Context context,String path){
        this.liveData=liveData;
        list = liveData.getValue();
        this.context=context;
        this.favorite_object_path = path;
    }
    @NonNull
    @Override
    public FavoriteThingAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        ViewDataBinding binding =  DataBindingUtil.inflate(inflater, R.layout.favorite_object,parent,false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteThingAdapter.Holder holder, int position) {
        FavoriteObjectItem item = list.get(position);
        String path = item.getPathName();
        Bitmap bitmap =getBitmap(path);
        item.setBitmap(bitmap);
        holder.bind(item,position);
    }


    @Override
    public int getItemCount() {
        return list == null ? 0: list.size();
    }

    public class Holder extends RecyclerView. ViewHolder {
        ViewDataBinding binding;
        public Holder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bind(Object obj,final int position){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context =v.getContext();
                    Intent intent =new Intent(context, ContentsActivity.class);
                    intent.putExtra("title",list.get(getAdapterPosition()).getPathName());
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity)v.getContext();
                    String nameTodelete = list.get(position).getPathName();
                    String folderDeletePath = activity.getFilesDir()+File.separator+nameTodelete; // 폴더
                    String imageDeletePath = favorite_object_path+File.separator+nameTodelete; // 이미지
                    BottomMainDialogFragment fragment = new BottomMainDialogFragment(liveData,position,folderDeletePath,imageDeletePath,favorite_object_path);
                    fragment.show(activity.getSupportFragmentManager(),"");
                    return false;
                }
            });

            binding.setVariable(BR.obj,obj);
            binding.executePendingBindings();
        }
    }


    Bitmap getBitmap(String fileName){
        InputStream inputStream=null;
        try {
            inputStream = new FileInputStream(new File(favorite_object_path+File.separator+fileName)); //try 에러처리 NULL
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(inputStream);
    }

    @BindingAdapter("imageUrl")
    public static void bindImage(final ImageView imageView, Bitmap bitmap) {
   //     imageView.setImageBitmap(bitmap);
        final Context context = imageView.getContext();

        Glide.with(context)
                .load(bitmap)
                .circleCrop()
                .into(imageView);
    }

}

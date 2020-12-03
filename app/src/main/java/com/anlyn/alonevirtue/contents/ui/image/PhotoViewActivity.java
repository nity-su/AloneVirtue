package com.anlyn.alonevirtue.contents.ui.image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.anlyn.alonevirtue.databinding.ActivityPhotoviewPagerBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class PhotoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPhotoviewPagerBinding binding =ActivityPhotoviewPagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        List<ImageInfo> urlList = (List) intent.getParcelableArrayListExtra("imageUrl");
        int currentPos = intent.getIntExtra("position",0);
        ViewPager2 viewPager2 = binding.photoViewPager2;
        PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(urlList);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(currentPos,false);
//        viewPager2.setCurrentItem(currentPos);

    }

    @BindingAdapter({"photoViewURl"})
    static public void loadUrl(PhotoView view,String url){
        Glide.with(view.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }
}
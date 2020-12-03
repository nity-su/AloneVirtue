package com.anlyn.alonevirtue.first_exe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.ActivityFirstExeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class FirstExeActivity extends AppCompatActivity {
    private ActivityFirstExeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstExeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoadPicture loadPicture =new LoadPicture(FirstExeActivity.this);
        initViewPager2(loadPicture.getList());
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.FirstExtVP2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        });
        mediator.attach();
    }

    void initViewPager2(List<FirstExeCellUnit> list){
        FirstExeVP2Adapter adapter = new FirstExeVP2Adapter(list);
        binding.FirstExtVP2.setAdapter(adapter);
    }

    @BindingAdapter("setDrawable")
    public static void setDrawable(ImageView imageView,int drawableRes){
        Context context =imageView.getContext();
        imageView.setImageDrawable(context.getDrawable(drawableRes));
    }
}
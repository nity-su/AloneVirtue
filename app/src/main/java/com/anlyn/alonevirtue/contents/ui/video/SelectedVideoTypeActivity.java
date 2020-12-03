package com.anlyn.alonevirtue.contents.ui.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.anlyn.alonevirtue.databinding.ActivitySelectedVideoTypeBinding;

public class SelectedVideoTypeActivity extends AppCompatActivity {
    ActivitySelectedVideoTypeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivitySelectedVideoTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init(){
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;
        int height = metrics.heightPixels;

        getWindow().setLayout((int)(width*.65),(int)(height*.1));

        LinearLayout linear =binding.selectedVideoTypeLinear;
        ViewGroup.LayoutParams params = linear.getLayoutParams();
        params.width = (int)(width*.65);
        params.height = (int)(height*.1);
        linear.setLayoutParams(params);


    }
}
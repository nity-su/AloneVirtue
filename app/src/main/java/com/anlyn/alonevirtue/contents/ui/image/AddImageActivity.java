package com.anlyn.alonevirtue.contents.ui.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.ActivityAddImageBinding;
import com.anlyn.alonevirtue.share_prefer.SharedPreferCtrl;
import com.bumptech.glide.Glide;

public class AddImageActivity extends AppCompatActivity {
    final int REQUET_CODE = 1010;
    private ActivityAddImageBinding binding;
    private ImageView imageView;
    private String type;
    private String uriString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferCtrl ctrl = new SharedPreferCtrl(AddImageActivity.this,"theme");
        ctrl.applyTheme("theme");

        binding = ActivityAddImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Intent intent =getIntent();
        type= intent.getStringExtra("image_type");
        imageView = binding.AddFavorImageIv;
        TableRow tableRow = binding.urlTableRow;

        //get Display size
//        DisplayMetrics metrics =new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int width=metrics.widthPixels;
//        int height = metrics.heightPixels;
//        //set Activity size
//        getWindow().setLayout((int)(width*.7),(int)(height*.8));


        Button btn = binding.addImageSaveBtn;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                if(type.equals("NETWORK"))
                intent.putExtra("url",binding.addImageUrlEV.getText().toString());
                if(type.equals("GALLERY"))
                    intent.putExtra("url",uriString);
                setResult(1010,intent);
                finish();
            }
        });

        if(type.equals("NETWORK")){
            imageView.setVisibility(View.INVISIBLE);
            tableRow.setVisibility(View.VISIBLE);
        }else if(type.equals("GALLERY")) {
//            constraintSetInit(width,height);
//            imageView.setVisibility(View.VISIBLE);
            tableRow.setVisibility(View.INVISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent =new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(galleryIntent,REQUET_CODE);

                }
            });
        }
        initToolbar();

    }

    void initToolbar(){

        final int start = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        RelativeLayout layout =new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageButton button =new ImageButton(getApplicationContext());
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnParams.setMarginStart(start);
        button.setLayoutParams(btnParams);
        button.setBackground(getDrawable(R.drawable.ic_baseline_arrow_back_ios_48));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        layout.addView(button,lp);

        binding.AddImageToolBar.addView(layout);

    }

    int caluLength(int orign,float ratio){
        return (int)(orign*ratio);
    }

    void constraintSetInit(int width,int height){
        ConstraintSet set = new ConstraintSet();

        ConstraintLayout layout =binding.AddImageConstLayout;
        imageView =binding.AddFavorImageIv;
//        ConstraintLayout.LayoutParams params =new ConstraintLayout.LayoutParams(caluLength(width,0.3f),caluLength(height,0.38f));
//        params.leftMargin=250;

        set.constrainWidth(imageView.getId(),caluLength(width,0.3f));
        set.constrainHeight(imageView.getId(),caluLength(height,0.38f));
        set.setMargin(imageView.getId(),ConstraintSet.TOP,caluLength(height,0.2f));
        set.centerHorizontally(imageView.getId(),layout.getId());
        set.applyTo(layout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUET_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Glide.with(getApplicationContext()).load(uri).into(imageView);
            uriString=uri.toString();
        }

    }


}
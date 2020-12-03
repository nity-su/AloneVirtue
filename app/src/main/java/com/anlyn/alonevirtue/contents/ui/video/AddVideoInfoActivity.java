package com.anlyn.alonevirtue.contents.ui.video;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.Toast;

import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.contents.ContentsActivity;
import com.anlyn.alonevirtue.contents.ui.image.AddImageActivity;
import com.anlyn.alonevirtue.databinding.ActivityAddVideoInfoBinding;
import com.anlyn.alonevirtue.main.MainActivity;
import com.anlyn.alonevirtue.share_prefer.SharedPreferCtrl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.greenfrvr.hashtagview.HashtagView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Cancellable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddVideoInfoActivity extends AppCompatActivity {
    private static final int SELECT_VIDEO = 1;
    private VideoContentsInfo info;
    private ActivityAddVideoInfoBinding binding;
    private ImageView imageView;
    private EditText titleEditText;
    private EditText urlEditText;
    private Intent intent;
    private String dataValue;
    private final List<String> hashTags =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferCtrl ctrl = new SharedPreferCtrl(AddVideoInfoActivity.this,"theme");
        ctrl.applyTheme("theme");

        binding = ActivityAddVideoInfoBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);
        init();
    }

    void init(){
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;
        int height = metrics.heightPixels;

//        getWindow().setLayout((int)(width*.7),(int)(height*.8));

        Button btn =binding.addVideoCheckBtn;
        Button tagAddBtn = binding.addTagBtn;
        TableRow title_tr = binding.addVideoTitleTr;
        final EditText tagEditText = binding.tagEditText;
        final HashtagView hashtagView = binding.hashTagView;
        final CheckBox checkBox =binding.checkBox;
        titleEditText =binding.AddVideoTitle;
        urlEditText = binding.AddVideoUrl;
        final TableRow tableRow = binding.addVideoUrlTr;
        imageView = binding.addImageThumbnailIv;

        Intent intent =getIntent();
        final String video_type = intent.getStringExtra("video_type");
        String DialogItems[] = getResources().getStringArray(R.array.content_type);
        //[0] -> 네트워크
        Log.d("video_type",DialogItems[0]);
        if(DialogItems[0].equals(video_type)){
            tableRow.setVisibility(View.VISIBLE);
            title_tr.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_PICK);
                imageIntent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"video/*");
                startActivityForResult(imageIntent, SELECT_VIDEO);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                if(video_type.equals("NETWORK")) {
                    intent.putExtra("url", urlEditText.getText().toString());
                    intent.putExtra("urlType", "NETWORK");
                }
                if(video_type.equals("GALLERY")){
                    intent.putExtra("urlType", "GALLERY");
                    intent.putExtra("url",getDataValue());
                }
                String title = checkBox.isChecked() ? "":titleEditText.getText().toString();
                intent.putExtra("name",title);
                intent.putExtra("tags", (Serializable) hashTags);
                intent.putExtra("titleAuto",checkBox.isChecked());
                setResult(1010, intent);
                finish();

            }
        });
//https://stackoverflow.com/questions/3554377/handling-click-events-on-a-drawable-within-an-edittext?page=1&tab=votes#tab-top
        tagEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (tagEditText.getWidth() - tagEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(hashTags.size()==3) {
                            tagEditText.setText("최대 3개 까지");
                            return true;
                        }
                        if(tagEditText.getText().toString().equals(""))
                            return true;

                        hashTags.add(tagEditText.getText().toString());
                        hashtagView.setData(hashTags,HASH);

                        return true;
                    }
                }
                return false;
            }
        });

        hashtagView.setData(hashTags,HASH);

        tagEditText.addTextChangedListener(new TextWatcher() {
            String preValue;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tagEditText.getText().toString().contains(" ")) {
                    Toast toast = Toast.makeText(getApplicationContext(),"띄어쓰기 금지 ",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    tagEditText.setText(tagEditText.getText().toString().replace(" ", ""));
                    tagEditText.setSelection(tagEditText.getText().length());
                }

                int length = tagEditText.getText().length();

                if(length>=10){
                    tagEditText.setText(preValue);
                    tagEditText.setSelection(tagEditText.getText().length());
                }else{
                    preValue = tagEditText.getText().toString();
                }
            }
        });
//        hashtagView.addOnTagClickListener(new HashtagView.TagsClickListener() {
//            @Override
//            public void onItemClicked(final Object item) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                hashTags.remove(item.toString());
//                                hashtagView.setData(hashTags,HASH);
//                            }
//                        });
//                    }
//                }).start();
//
//            }
//        });

        Observable<Object> searchTextObservable = createItemClickObservable(hashtagView);
        searchTextObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object item) throws Throwable {
                        hashTags.remove(item.toString());
                        hashtagView.setData(hashTags,HASH);
                    }
                });

    }
    public final HashtagView.DataTransform<String> HASH = new HashtagView.DataTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            SpannableString spannableString = new SpannableString("#" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#483BFF")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //
            return spannableString;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_VIDEO && resultCode == RESULT_OK){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.isMemoryCacheable();
            Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(data.getData()).into(imageView);
            setDataValue(data.getData().toString());
        }
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    private Observable<Object> createItemClickObservable(final HashtagView hashtagView) {

        // 2
        return Observable.create(new ObservableOnSubscribe<Object>() {

            // 3
            @Override
            public void subscribe(final ObservableEmitter<Object> emitter) throws Exception {
                // 4
                final HashtagView.TagsClickListener listener =new HashtagView.TagsClickListener(){
                    @Override
                    public void onItemClicked(Object item) {
                        emitter.onNext(item);
                    }
                };
                hashtagView.addOnTagClickListener(listener);


                // 6
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        // 7
                        hashtagView.removeOnTagClickListener(listener);
                    }
                });
            }
        });
    }

}
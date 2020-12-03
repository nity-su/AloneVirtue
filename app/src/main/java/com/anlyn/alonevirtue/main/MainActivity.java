package com.anlyn.alonevirtue.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anlyn.alonevirtue.CreateFavoriteObjectActivity;
import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.ActivityMainBinding;
import com.anlyn.alonevirtue.databinding.ToolBarBinding;
import com.anlyn.alonevirtue.first_exe.FirstExeActivity;
import com.anlyn.alonevirtue.json_mangement.JsonDataRead;
import com.anlyn.alonevirtue.json_mangement.JsonDataSave;
import com.anlyn.alonevirtue.json_mangement.JsonTransHandler;
import com.anlyn.alonevirtue.share_prefer.SharedPreferCtrl;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends MainAppCompatActivity implements View.OnClickListener {
    private SharedPreferences preferences;
    private ActivityMainBinding mainBinding;
    private FavoriteThingAdapter adapter;
    private final int REQUEST_CODE=1001;
    private String favor;
    private FavoriteObjectJsonHandler handler;
    private String favorite_object_path;
    private ViewModelMainActivity viewModel;

    public interface ActionClick{
        void onClick();
    }

    private Map viewActionMap = new HashMap<View,ActionClick>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferCtrl ctrl = new SharedPreferCtrl(MainActivity.this,"theme");
        ctrl.applyTheme("theme");
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view =mainBinding.getRoot();
        setContentView(view);
        mkdir();
        viewModel = new ViewModelProvider(this).get(ViewModelMainActivity.class);
        viewModel.populateData(getApplicationContext(),favorite_object_path);
        mainBinding.mToolBar.inflateMenu(R.menu.actionbar_actions);
//        DelCacheDirAndFile.deleteCache(getApplicationContext());
        initRecyclearView(viewModel.getLiveData());
        changeToolbarFont(mainBinding.mToolBar,MainActivity.this);
        setSupportActionBar(mainBinding.mToolBar);
        AdsUtil adsUtil = new AdsUtil(MainActivity.this,mainBinding.adView);
        adsUtil.show();
        if(ctrl.isOpenAppFirst()) {
            guide();
            ctrl.appWasOpened(false);
        }
    }
    void actionBarSetting(){
        final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
        final int endMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());

        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        tv.setText("목록");
        tv.setTextSize(24);
        //font
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/roboto_medium.ttf");
        tv.setTypeface(tf);
        // font color
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.toolbarTitleColor, typedValue, true);
        int color = ContextCompat.getColor(this, typedValue.resourceId);
        tv.setTextColor(color);
        relativeLayout.addView(tv,lp);

        lp = new RelativeLayout.LayoutParams(width, height);
        lp.setMarginEnd(endMargin);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        ImageButton createItemBtn = new ImageButton(getApplicationContext());
        createItemBtn.setId(View.generateViewId());
        createItemBtn.setBackground(getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
        createItemBtn.setOnClickListener(this);
        viewActionMap.put(createItemBtn,createBtnListener());
        relativeLayout.addView(createItemBtn,lp);

        lp = new RelativeLayout.LayoutParams(width, height);
        lp.setMarginEnd(endMargin);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.addRule(RelativeLayout.LEFT_OF,createItemBtn.getId());
        ImageButton settingBtn = new ImageButton(getApplicationContext());
        settingBtn.setBackground(getDrawable(R.drawable.ic_baseline_settings_24));
        settingBtn.setOnClickListener(this);
        viewActionMap.put(settingBtn,settingBtnListener());
        relativeLayout.addView(settingBtn,lp);

        Toolbar toolbar = mainBinding.mToolBar;
        toolbar.addView(relativeLayout);
//        setSupportActionBar(mainBinding.mToolBar);
    }

    void guide(){

       final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        // Likewise, this tap target will target the search button
                        TapTarget.forToolbarOverflow(mainBinding.mToolBar,"사용법을 누르면 설명 창으로 넘어갑니다..")
                                .dimColor(R.color.colorPrimary)
                                .outerCircleColor(R.color.colorAccent)
                                .targetCircleColor(R.color.colorPrimary)
                                .textColor(android.R.color.black)
                                .cancelable(false)
                                .id(1))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        mainBinding.mToolBar.showOverflowMenu();
                    }


                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });

            sequence.start();
    }
    void guilde(){
//        mainBinding.mToolBar.;
//        findOverflowMenuButton(MainActivity.this,mainBinding.mToolBar);
//        Target target = new Target() {
//            @Override
//            public Point getPoint() {
//                // Get approximate position of home icon's center
//                int height = mainBinding.mToolBar.getHeight();
//                int width =  mainBinding.mToolBar.getWidth();
//                int x = width;
//                int y = height;
//                return new Point(x, y);
//            }
//        };
//        new ShowcaseView.Builder(this)
//                .setTarget(target)
//                .setContentTitle("사용법이 있습니다.")
//                .setContentText("")
//                .hideOnTouchOutside()
//                .build();
    }
    void mkdir(){
        favorite_object_path =getFilesDir()+File.separator+"FavoriteObject";
        File file =new File(favorite_object_path);
        if(!file.exists()) {
            file.mkdir();
        }
    }

    void initRecyclearView(MutableLiveData<List<FavoriteObjectItem>> liveData){

        RecyclerView recyclerView= mainBinding.FavoriteStarRV;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoriteThingAdapter(liveData,getApplicationContext(),favorite_object_path);
        recyclerView.setAdapter(adapter);
        viewModel.getLiveData().observe(this, new Observer<List<FavoriteObjectItem>>() {
            @Override
            public void onChanged(List<FavoriteObjectItem> list) {
                adapter.setList(list);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이 과정을 progress로
        if(requestCode==REQUEST_CODE && 1010 == resultCode){
            List<FavoriteObjectItem> list = new ArrayList<>();;
            String name= data.getStringExtra("name");
            String bitmapName= data.getStringExtra("bitmapName");
            Log.d("name: ", name);
            list = viewModel.getLiveData().getValue();
            list.add(new FavoriteObjectItem(bitmapName,name));


            FavoriteObjectJsonHandler handler =new FavoriteObjectJsonHandler();
            handler.setList(list);
            JSONObject jsonObject = (JSONObject)handler.encode(JsonTransHandler.OBJECT_TO_JSON);
            //create json file
            JsonDataSave jsonDataSave =new JsonDataSave();
            String path = favorite_object_path+File.separator+"FavoriteObject";
            jsonDataSave.create(getApplicationContext(),path,jsonObject.toString());

            //create dir
            for(int i=0;i<list.size();i++){
                File file = new File(getFilesDir()+File.separator+list.get(i).getPathName());

                if(!file.exists()){
                    file.mkdir();
                }
            }

            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }



    }


    @Override
    public void onClick(View v) {
        ActionClick actionClick=(ActionClick) viewActionMap.get(v);
        actionClick.onClick();
    }

    ActionClick settingBtnListener(){
        return new ActionClick(){
            @Override
            public void onClick() {
                ThemeSetDialog themeSetDialog =new ThemeSetDialog(MainActivity.this);
                AlertDialog dialog = themeSetDialog.getBuilder().create();
                dialog.show();
            }
        };
    }

    ActionClick createBtnListener(){
        return new ActionClick() {
            @Override
            public void onClick() {
                Intent intent =new Intent(getApplicationContext(), CreateFavoriteObjectActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_plus:
                Intent intent =new Intent(getApplicationContext(), CreateFavoriteObjectActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                return true;
            case R.id.action_setting:
                ThemeSetDialog themeSetDialog =new ThemeSetDialog(MainActivity.this);
                AlertDialog dialog = themeSetDialog.getBuilder().create();
                dialog.show();
                return true;
            case R.id.action_question:
                startActivity(new Intent(getApplicationContext(), FirstExeActivity.class));
                 return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    applyFont(tv, context);
                    break;
                }
            }
        }
    }


    public void applyFont(TextView tv, Activity context) {
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/roboto_medium.ttf"));
    }
}
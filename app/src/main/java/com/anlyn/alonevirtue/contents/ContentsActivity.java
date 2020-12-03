package com.anlyn.alonevirtue.contents;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.anlyn.alonevirtue.MobileNavigationDirections;
import com.anlyn.alonevirtue.R;
import com.anlyn.alonevirtue.databinding.ActivityContentsBinding;
import com.anlyn.alonevirtue.share_prefer.SharedPreferCtrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ContentsActivity extends ContentsAppCompatActivity  {
    private ActivityContentsBinding binding;
    private Activity activity;
    private SharedPreferences preferences;
    private final int READ_STORAGE_PERMISSION_REQUEST_CODE =1011;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferCtrl  sharedPreferCtrl = new SharedPreferCtrl(ContentsActivity.this,"theme");
        sharedPreferCtrl.applyTheme("theme");
        binding= ActivityContentsBinding.inflate(getLayoutInflater());
        toolbar =binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_search);
        View view = binding.getRoot();
        setContentView(view);

        BottomNavigationView navView = binding.navView;

        activity=this;

        Intent intent =getIntent();
        final String title =intent.getStringExtra("title");
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_image, R.id.navigation_video)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_image:

                        navController.popBackStack();
//                        menuItem.setChecked(true);
                        MobileNavigationDirections.ToNavigationImage actionToImage = MobileNavigationDirections.toNavigationImage();
                        actionToImage.setTitle(title);
                        Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(actionToImage);
                        break;

                    case R.id.navigation_video:
                        navController.popBackStack();
//                        menuItem.setChecked(true);
                        MobileNavigationDirections.ToNavigationVideo actionToViedo = MobileNavigationDirections.toNavigationVideo();
                        actionToViedo.setTitle(title);
                        Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(actionToViedo);
                        break;
//                }
                }

                return true;
            }
        });
        navController.popBackStack();
        MobileNavigationDirections.ToNavigationImage actionToImage = MobileNavigationDirections.toNavigationImage();
        actionToImage.setTitle(title);
        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(actionToImage);

        checkPermissionForReadExtertalStorage();
        try {
            requestPermissionForReadExtertalStorage();
        } catch (Exception e) {e.printStackTrace();

        }
    }


    public boolean checkPermissionForReadExtertalStorage() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                return result == PackageManager.PERMISSION_GRANTED;
            }
            return false;

    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_search, menu);
//        Log.d("eee","eee");
//        MenuItem searchItem = menu.findItem(R.id.toolbar);
//
//        SearchManager searchManager = (SearchManager) ContentsActivity.this.getSystemService(Context.SEARCH_SERVICE);
//
//        androidx.appcompat.widget.SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(ContentsActivity.this.getComponentName()));
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

}
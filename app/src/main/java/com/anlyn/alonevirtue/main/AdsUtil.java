package com.anlyn.alonevirtue.main;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.anlyn.alonevirtue.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.List;

class AdsUtil {
    private Context context;
    private AdView mAdView;

    public AdsUtil(Context context, AdView mAdView) {
        this.context = context;
        this.mAdView = mAdView;

        mAdView.setAdListener(new AdListener() {
            @Override

            public void onAdLoaded() {

                // Code to be executed when an ad finishes loading.

                // 광고가 문제 없이 로드시 출력됩니다.

                Log.d("@@@", "onAdLoaded");

            }



            @Override

            public void onAdFailedToLoad(int errorCode) {

                // Code to be executed when an ad request fails.

                // 광고 로드에 문제가 있을시 출력됩니다.

                Log.d("@@@", "onAdFailedToLoad " + errorCode);

            }



            @Override

            public void onAdOpened() {

                // Code to be executed when an ad opens an overlay that

                // covers the screen.

            }



            @Override

            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.

            }



            @Override

            public void onAdLeftApplication() {

                // Code to be executed when the user has left the app.

            }



            @Override

            public void onAdClosed() {

                // Code to be executed when the user is about to return

                // to the app after tapping on an ad.

            }

        });

    }

    public void show(){

        MobileAds.initialize(context);
//        List<String> testDeviceIds = Arrays.asList(context.getString(R.string.admob_app_id));
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        MobileAds.setRequestConfiguration(configuration);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }




}

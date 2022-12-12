package com.example.engine_android.Modules;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdSystemAndroid {
    // app context
    Context context;

    // ad banner
    AdView adView;

    public AdSystemAndroid(Context context) {
        this.context = context;
        this.adView = null;

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                System.out.println("Ad system properly initialized");
            }
        });
    }

    public void loadBannerAd(AdView adView) {
        this.adView = adView;
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void showBannerAd(boolean show) {
        if(this.adView == null) {
            System.out.println("Banner doesn't exists yet");
            return;
        }

        // set visibility
        this.adView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}

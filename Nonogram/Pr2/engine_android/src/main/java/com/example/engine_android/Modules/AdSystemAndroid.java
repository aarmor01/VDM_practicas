package com.example.engine_android.Modules;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdSystemAndroid {
    // reward token
    private final String REWARD_AD_TOKEN = "ca-app-pub-3940256099942544/5224354917";

    // app context and activity
    private Context context;
    private AppCompatActivity activity;

    // ad request
    private AdRequest adRequest;

    // ad banner
    private AdView bannerAd;

    // ad reward
    private RewardedAd rewardAd;
    private boolean rewardGranted;

    public AdSystemAndroid(AppCompatActivity activity, Context context) {
        // context
        this.context = context;
        this.activity = activity;

        // ads
        this.bannerAd = null;
        this.rewardAd = null;
        this.rewardGranted = false;

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                System.out.println("Ad system properly initialized");
            }
        });

        // ad request
        this.adRequest = new AdRequest.Builder().build();
    }

    // Banner
    public void loadBannerAd(AdView adView) {
        this.bannerAd = adView;
        adView.loadAd(this.adRequest);
    }

    public void showBannerAd(boolean show) {
        if (this.bannerAd == null) {
            System.out.println("Banner doesn't exists yet");
            return;
        }

        // set visibility
        this.bannerAd.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // Rewarded
    public void loadRewardedAd() {
        RewardedAd.load(this.activity, REWARD_AD_TOKEN, adRequest,
                new MyRewardedAdLoadCallback());
    }

    public void showRewardedAd() {
        this.activity.runOnUiThread(new ShowRewardThread(this.activity));
    }

    public boolean hasRewardBeenGranted() {
        // if granted, reset boolean
        if (this.rewardGranted) {
            this.rewardGranted = false;
            return true;
        }

        // else, we just send that it hasn't been granted
        return false;
    }

    private class ShowRewardThread implements Runnable {
        private AppCompatActivity activity;

        ShowRewardThread(AppCompatActivity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            // if ad hasn't load
            if (rewardAd == null)
                System.out.println("Reward ad not set up yet.");
            else {
                // show ad (we assume one has been preloaded before)
                rewardAd.setFullScreenContentCallback(new MyFullScreenContentCallback());
                rewardAd.show(this.activity, new MyUserEarnedRewardListener());
                rewardAd = null;
            }

            // load ad for next showing
            loadRewardedAd();
        }
    }

    private class MyUserEarnedRewardListener implements OnUserEarnedRewardListener {
        @Override
        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
            // Handle the reward.
            System.out.println("Rewarded granted.");
            rewardGranted = true;
            //int rewardAmount = rewardItem.getAmount();
            //String rewardType = rewardItem.getType();
        }
    }

    // load callback
    private class MyRewardedAdLoadCallback extends RewardedAdLoadCallback {
        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            // Handle the error.
            System.err.println("Rewarded ad not properly loaded");
            rewardAd = null;
        }

        @Override
        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
            // obtain data
            System.out.println("Rewarded ad properly loaded");
            rewardAd = rewardedAd;
        }
    }

    // full screen callback
    private class MyFullScreenContentCallback extends FullScreenContentCallback {
        @Override
        public void onAdClicked() {
            // Called when a click is recorded for an ad.
            // Log.d(TAG, "Ad dismissed fullscreen content.");
            System.out.println("Ad was clicked.");
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
            // Set the ad reference to null so you don't show the ad a second time.
            System.err.println("Ad dismissed fullscreen content.");
        }

        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            // Called when ad fails to show.
            System.err.println("Ad failed to show fullscreen content.");
            rewardAd = null;
        }

        @Override
        public void onAdImpression() {
            // Called when an impression is recorded for an ad.
            System.out.println("Ad recorded an impression.");
        }

        @Override
        public void onAdShowedFullScreenContent() {
            // Called when ad is shown.
            System.out.println("Ad showed fullscreen content.");
        }
    }
}

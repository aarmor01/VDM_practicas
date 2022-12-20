package com.example.app_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.google.android.gms.ads.AdView;

import com.example.app_android.Scenes.BootScene;

import com.example.engine_android.EngineAndroid;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// onPause() ➟ onStop() ➟ onSaveInstanceState() ➟ onDestroy() ➟ Same Activity Opened Again ➟
// onCreate() ➟ onStart() ➟ onRestoreInstanceState() ➟ onResume()

public class MainActivity extends AppCompatActivity {
    // engine's window ratio and background colour
    final int WIDTH = 400, HEIGHT = 600;
    final int BACKGROUND_COLOR = 0xFFFFFFFF;
    final long NOTIFICATION_PUSH = 30;

    // engine
    private EngineAndroid engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set surface view
        setContentView(R.layout.activity_main);
        SurfaceView renderView = findViewById(R.id.surfaceView);

        // fullscreen and remove support action bar
        activityConfigurations();

        // create engine
        engine = new EngineAndroid(renderView, this, WIDTH, HEIGHT, BACKGROUND_COLOR);

        // start ad process
        this.engine.getAdSystem().loadBannerAd((AdView)findViewById(R.id.adView));
        this.engine.getAdSystem().loadRewardedAd();

        //creates notification channel
        this.engine.getIntentSystem().createChannel("Nonogram",
                "Notifications for Nonogram App", "not_nonogram");

        // load game data
        GameManager.init(WIDTH, HEIGHT, this.engine, savedInstanceState);
        this.engine.getRender().setBackGroundColor(GameManager.getInstance().getColor(0));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // if the onCreate method doesn't work, we load data here
        if (GameManager.getInstance() == null)
            GameManager.init(WIDTH, HEIGHT, this.engine, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //cancel notifications
        WorkManager.getInstance(this).cancelAllWork();
        // create boot scene
        BootScene scene = new BootScene();
        this.engine.getSceneManager().changeScene(scene, engine);

        // resume engine process cycle
        this.engine.resume();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // updates the orientation of the screen
        this.engine.updateConfiguration(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // pause engine process cycle
        this.engine.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // notifications done on app closure
        createWorkRequest("not_nonogram", "Nonograms need you!", "Time to play some Nonograms and save the world");
        startPeriodicWorkRequest("not_nonogram", "We miss you!", "If you don't play all of us will starve to death");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GameManager.shutdown();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // save data
        GameManager.save(this.engine, outState);
    }

    private void activityConfigurations() {
        // fullscreen
        if (Build.VERSION.SDK_INT < 16)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        // hide support bar
        getSupportActionBar().hide();
    }

    // notification tryout?
    private void createWorkRequest(String channel_id, String title, String text) {
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("chanel", channel_id);
        dataValues.put("smallIcon", androidx.constraintlayout.widget.R.drawable.notification_template_icon_low_bg);
        dataValues.put("contentTitle", title);
        dataValues.put("contentText", text);
        Data inputData = new Data.Builder().putAll(dataValues).build();

        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(IntentWork.class)
                        // Additional configuration
                        .addTag("notifications")
                        .setInitialDelay(NOTIFICATION_PUSH, TimeUnit.SECONDS)
                        .setInputData(inputData)
                        .build();

        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
    }

    private void startPeriodicWorkRequest(String channel_id, String title, String text) {
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("chanel", channel_id);
        dataValues.put("smallIcon", androidx.constraintlayout.widget.R.drawable.notification_template_icon_low_bg);
        dataValues.put("contentTitle", title);
        dataValues.put("contentText", text);
        Data inputData = new Data.Builder().putAll(dataValues).build();

        PeriodicWorkRequest uploadWorkRequestPeriodic =
                new PeriodicWorkRequest.Builder(IntentWork.class,
                        15, TimeUnit.MINUTES,
                        5, TimeUnit.MINUTES)
                        // Constraints
                        .addTag("notifications")
                        .setInputData(inputData)
                        .build();

        WorkManager.getInstance(this).enqueue(uploadWorkRequestPeriodic);
    }
}
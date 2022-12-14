package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.app_android.Scenes.MainMenu;
import com.example.engine_android.EngineAndroid;
import com.google.android.gms.ads.AdView;

// onPause() ➟ onStop() ➟ onSaveInstanceState() ➟ onDestroy() ➟ Same Activity Opened Again ➟
// onCreate() ➟ onStart() ➟ onRestoreInstanceState() ➟ onResume()

public class MainActivity extends AppCompatActivity {
    // engine's window ratio and background colour
    final int WIDTH = 400, HEIGHT = 600;
//    final float RATIO = WIDTH / HEIGHT;
    final int BACKGROUND_COLOR = 0xFFFFFFFF;

    // engine
    EngineAndroid engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set surface view
        setContentView(R.layout.activity_main);
        SurfaceView renderView = findViewById(R.id.surfaceView);

        // fullscreen and remove support action bar
        activityConfigurations();

        // create engine
        engine = new EngineAndroid(renderView, this, this.getBaseContext(), WIDTH, HEIGHT, BACKGROUND_COLOR);

        // start ad process
        engine.getAdSystem().preloadBannerAd((AdView) findViewById(R.id.adView));
        engine.getAdSystem().preloadRewardedAd();

        // load files
        GameManager.init(WIDTH, HEIGHT, engine, savedInstanceState);
        engine.getRender().setBackGroundColor(GameManager.getInstance().getColor(0));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // create start scene if no scene has been initialised
        // (defensive code just in case, we should be fine with
        // just the onConfigurationChanged)
        if (engine.getSceneManager().isEmpty()) {
            MainMenu scene = new MainMenu();
            engine.getSceneManager().pushScene(scene);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // resume engine process cycle
        engine.resume();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // if the onCreate method doesn't work, we load data here
        if (GameManager.getInstance() == null)
            GameManager.init(WIDTH, HEIGHT, engine, savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            System.out.println("Landscape");
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            System.out.println("Portrait");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // pause engine process cycle
        engine.pause();
        engine.getAudio().stopMusic();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save data
        GameManager.shutdown(engine, outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // in case we haven't gone to onSaveInstanceState(), we
        // save data in this part of the lifecycle
        if (GameManager.getInstance() != null)
            GameManager.shutdown(engine, null);
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
}
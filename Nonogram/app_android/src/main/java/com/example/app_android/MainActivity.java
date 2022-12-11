package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.app_android.Scenes.MainMenu;
import com.example.engine_android.EngineAndroid;

// onPause() ➟ onStop() ➟ onSaveInstanceState() ➟ onDestroy() ➟ Same Activity Opened Again ➟
// onCreate() ➟ onStart() ➟ onRestoreInstanceState() ➟ onResume()

public class MainActivity extends AppCompatActivity {
    // engine's window ratio and background colour
    final float RATIO = 4.0f/6.0f;
    final int BACKGROUND_COLOR = 0xFFFFFFFF;

    // engine
    EngineAndroid engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set surface view
        SurfaceView renderView = new SurfaceView(this);
        setContentView(renderView);

        // fullscreen and remove support action bar
        viewConfig();

        // create engine
        engine = new EngineAndroid(renderView, this.getBaseContext(), RATIO, BACKGROUND_COLOR);

        // load files
        GameManager.init(engine, savedInstanceState);
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
//        // load files
//        GameManager.init(engine, savedInstanceState);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();

        // pause engine process cycle
//      engine.pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save files
        GameManager.shutdown(engine, outState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(GameManager.getInstance() != null)
            GameManager.shutdown(engine, null);
    }

    private void viewConfig() {
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
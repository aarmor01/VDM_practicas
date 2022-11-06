package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.engine_android.AudioAndroid;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.RenderAndroid;
import com.example.engine_common.interfaces.IAudio;
import com.example.nonogram.MyScene;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SurfaceView renderView;

    private EngineAndroid eng;

    private AssetManager aMan;

    private AudioAndroid audioManager;

    private int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = new SurfaceView(this);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(this.renderView);
        MyScene scene = new MyScene();
        aMan = this.getBaseContext().getAssets();
        eng = new EngineAndroid(this.renderView, aMan);
        audioManager = (AudioAndroid)eng.getAudio();
        audioManager.loadMusic("./assets/sounds/doFlauta.wav", 0.5f);
        scene.init(eng);
        eng.getSceneManager().pushScene(scene);
        audioManager.playMusic();
        eng.resume();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Log.d("tag", "config changed");
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ((RenderAndroid)eng.getRender()).changeScreen(true);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            ((RenderAndroid)eng.getRender()).changeScreen(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.render.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //this.render.pause();
    }
}
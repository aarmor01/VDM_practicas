package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.SurfaceView;

import com.example.engine_android.AudioAndroid;
import com.example.engine_android.EngineAndroid;
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
        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = new SurfaceView(this);
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
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
        eng = new EngineAndroid();
        aMan = this.getBaseContext().getAssets();
        eng.init(this.renderView, aMan);
        audioManager = (AudioAndroid)eng.getAudio();
        try {
            audioManager.setMusic("sounds/doFlauta.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }

        eng.setScene(scene);
        //audioManager.startMusic();
        eng.resume();

        //aMan = this.getBaseContext().getAssets();

        //listener = new MyListener();

        //soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        //listener.setSoundPool(soundPool);
        //renderView.setOnTouchListener(listener);

        // id sound
//        soundId = -1;
//        try {
//            AssetFileDescriptor assetDescriptor = aMan.openFd("sounds/doFlauta.wav");
//            soundId = soundPool.load(assetDescriptor, 0);
//            listener.setSoundId(soundId);
//        }catch (IOException e) {
//            throw new RuntimeException("Couldn't load sound.");
//        }
//
//        render.setAssetManager(aMan);
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
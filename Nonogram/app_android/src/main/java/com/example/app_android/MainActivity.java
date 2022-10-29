package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.SurfaceView;

import com.example.engine_android.Engine;
import com.example.nonogram.MyScene;

public class MainActivity extends AppCompatActivity {

    private SurfaceView renderView;

    private Engine eng;

    private AssetManager aMan;

    private SoundPool soundPool;

    private MyListener listener;

    private int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);
        MyScene scene = new MyScene();
        eng = new Engine();
        eng.init(this.renderView, aMan);
        eng.setScene(scene);
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
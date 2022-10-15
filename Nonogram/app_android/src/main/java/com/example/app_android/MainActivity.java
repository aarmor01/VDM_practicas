package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SurfaceView renderView;

    private MyRenderClass render;

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

        this.render = new MyRenderClass(this.renderView);
        scene.init(render);
        render.setScene(scene);
        aMan = this.getBaseContext().getAssets();

        listener = new MyListener();

        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        listener.setSoundPool(soundPool);
        renderView.setOnTouchListener(listener);

        // id sound
        soundId = -1;
        try {
            AssetFileDescriptor assetDescriptor = aMan.openFd("sounds/doFlauta.wav");
            soundId = soundPool.load(assetDescriptor, 0);
            listener.setSoundId(soundId);
        }catch (IOException e) {
            throw new RuntimeException("Couldn't load sound.");
        }

        render.setAssetManager(aMan);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.render.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.render.pause();
    }
}
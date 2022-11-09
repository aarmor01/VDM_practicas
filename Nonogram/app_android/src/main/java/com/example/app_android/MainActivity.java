package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.engine_android.EngineAndroid;
import com.example.nonogram.MainMenu;

public class MainActivity extends AppCompatActivity {
    private SurfaceView renderView;
    private EngineAndroid eng;
    private AssetManager aMan;

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
        MainMenu scene = new MainMenu(eng);
        aMan = this.getBaseContext().getAssets();
        eng = new EngineAndroid(this.renderView, aMan);
        eng.getSceneManager().pushScene(scene);
        eng.resume();

    }
}
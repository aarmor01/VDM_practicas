package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.engine_android.EngineAndroid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set surface view
        SurfaceView renderView = new SurfaceView(this);
        setContentView(renderView);

        // fullscreen and remove support action bar
        if (Build.VERSION.SDK_INT < 16)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        getSupportActionBar().hide();

        // create engine and scene
        Context context = this.getBaseContext();
        EngineAndroid eng = new EngineAndroid(renderView, context, 4.0f/6.0f, 0xFFFFFFFF);
        MainMenu scene = new MainMenu();

        // start up
        eng.getSceneManager().pushScene(scene);
        eng.resume();

        // test
        CategoryData ct = null;
        try
        {
            // Reading the object from a file
            FileInputStream file = context.openFileInput("save.bin");
            ObjectInputStream in = new ObjectInputStream(file);
            // Method for deserialization of object
            ct = (CategoryData)in.readObject();
            in.close();
            file.close();
            System.out.println("Object has been deserialized ");
        } catch(Exception ex) {
            ct = new CategoryData();
            System.out.println("Exception is caught");
        }

        ct.levelUnlocked++;

        try
        {
            // Reading the object from a file
            FileOutputStream file = context.openFileOutput("save.bin", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(file) ;
            // Method for serialization of object
            out.writeObject(ct) ;
            out.close() ;
            file.close() ;
            System.out.println("Object has been deserialized ");
        } catch(Exception ex) {
            System.out.println("Exception is caught");
        }
    }
}
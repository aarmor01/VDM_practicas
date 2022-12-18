package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.app_android.Scenes.MainMenu;
import com.example.engine_android.EngineAndroid;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// onPause() ➟ onStop() ➟ onSaveInstanceState() ➟ onDestroy() ➟ Same Activity Opened Again ➟
// onCreate() ➟ onStart() ➟ onRestoreInstanceState() ➟ onResume()

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // engine's window ratio and background colour
    final float RATIO = 4.0f/6.0f;
    final int BACKGROUND_COLOR = 0xFFFFFFFF;
    final long NOTIFICATION_PUSH = 30;

    // engine
    EngineAndroid engine;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set sensor and sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager .registerListener( this, sensor , SensorManager.SENSOR_DELAY_NORMAL);

        // set surface view
        setContentView(R.layout.activity_main);
        SurfaceView renderView = findViewById(R.id.surfaceView);

        // fullscreen and remove support action bar
        activityConfigurations();

        // create engine
        engine = new EngineAndroid(renderView, this, this.getBaseContext(), RATIO, BACKGROUND_COLOR);

        // start ad process
//        engine.getAdSystem().preloadBannerAd((AdView)findViewById(R.id.adView));
//        engine.getAdSystem().preloadRewardedAd();

        // load files
        GameManager.init(engine, savedInstanceState);
        engine.getRender().setBackGorundColor(GameManager.getInstance().getColor(0));

        createWorkRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // create start scene if no scene has been initialised
        // (defensive code just in case, we should be fine with
        // just the onConfigurationChanged)
        if (engine.getSceneManager().isEmpty()) {
            MainMenu scene = new MainMenu();
            engine.getSceneManager().changeScene(scene, engine);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // resume engine process cycle
        engine.resume();
        sensorManager .registerListener( this, sensor, SensorManager. SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // if the onCreate method doesn't work, we load data here
        if (GameManager.getInstance() == null)
            GameManager.init(engine, savedInstanceState);
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
        sensorManager .unregisterListener( this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save data
        GameManager.shutdown(engine, outState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // in case we haven't gone to onSaveInstanceState(), we
        // save data in this part of the lifecycle
        if(GameManager.getInstance() != null)
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

    private void createWorkRequest(){
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("chanel", "nonogram_prueba");
        dataValues.put("smallIcon", androidx.constraintlayout.widget.R.drawable.notification_template_icon_low_bg);
        //dataValues.put("mainClass", MainActivity.class);
        Data inputData = new Data.Builder().putAll(dataValues).build();
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(IntentWork.class)
                        // Additional configuration
                        .setInitialDelay(NOTIFICATION_PUSH, TimeUnit.SECONDS)
                        .setInputData(inputData)
                        .build();

        WorkManager.getInstance(this).enqueue(uploadWorkRequest);

        PeriodicWorkRequest uploadWorkRequestPeriodic =
                new PeriodicWorkRequest.Builder(IntentWork.class,
                        15, TimeUnit.MINUTES,
                        5, TimeUnit.MINUTES)
                        // Constraints
                        .setInputData(inputData)
                        .build();

        WorkManager.getInstance(this).enqueue(uploadWorkRequestPeriodic);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            float sensorValue = sensorEvent.values[0];
            // TODO
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (sensor.getType() == Sensor.TYPE_LIGHT) {

            // TODO
        }
    }
}
package com.example.engine_android.Modules;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Context context;


    public LightSensor(Context context) {
        this.context = context;
        // set sensor and sensor manager
        this.sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public void onResume() {
        // register listener
        this.sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        // sensor things i guess
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {}

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}

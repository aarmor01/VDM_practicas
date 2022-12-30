package com.example.app_android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Modules.LightSensor;

public class LightSensorApp extends LightSensor {
    final int NIGHT = 50;

    private EngineAndroid engRef;

    public LightSensorApp(Context c, EngineAndroid eng) {
        super(c);
        engRef = eng;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            float sensorValue = sensorEvent.values[0];
            if (sensorValue <= NIGHT)
                GameManager.getInstance().setNightMode(1);
            else
                GameManager.getInstance().setNightMode(0);

            engRef.getRender().setBackGroundColor(GameManager.getInstance().getColor(GameManager.ColorTypes.BG_COLOR.ordinal()));
        }
    }
}

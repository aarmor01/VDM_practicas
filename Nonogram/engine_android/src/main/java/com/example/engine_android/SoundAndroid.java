package com.example.engine_android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.example.engine_common.interfaces.ISound;

import java.io.File;
import java.io.IOException;

public class SoundAndroid implements ISound {
    //sound parameters
    private float volume;
    private int soundId;
    private int loop;
    private int priority;
    private float rate;

    public SoundAndroid(String filePath, AssetManager aMan, SoundPool soundPool, float volume_) {
        //loads the sound into the sound pool and stores the iD
        AssetFileDescriptor assetDescriptor = null;
        try {
            assetDescriptor = aMan.openFd(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        soundId = soundPool.load(assetDescriptor, 0);
        //sets all sound parameters
        volume = volume_;
        loop = 0;
        priority = 0;
        rate = 1.0f;
    }

    //setters and getters of every sound parameter
    public int getSoundId(){
        return soundId;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public void setVolume(float v) {
        volume = v;
    }
}

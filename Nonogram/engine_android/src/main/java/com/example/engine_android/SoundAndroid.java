package com.example.engine_android;

import com.example.engine_common.interfaces.ISound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import android.media.SoundPool;

public class SoundAndroid implements ISound {
    //sound parameters
    private int soundId;
    private float volume;

    private int loop;
    private int priority;
    private float rate;

    public SoundAndroid(String filePath, AssetManager aMan, SoundPool soundPool, float volume_) {
        // load the sound into the sound pool and stores the iD
        try {
            AssetFileDescriptor assetDescriptor = null;
            assetDescriptor = aMan.openFd(filePath);
            soundId = soundPool.load(assetDescriptor, 0);
        } catch (Exception e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }

        //sets all sound parameters
        volume = volume_;
        loop = 0;
        priority = 0;
        rate = 1.0f;
    }

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

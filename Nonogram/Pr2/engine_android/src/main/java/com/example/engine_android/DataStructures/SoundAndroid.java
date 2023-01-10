package com.example.engine_android.DataStructures;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import android.media.SoundPool;

public class SoundAndroid {
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
            this.soundId = soundPool.load(assetDescriptor, 1);
        } catch (Exception e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }

        //sets all sound parameters
        this.volume = volume_;
        this.loop = 0;
        this.priority = 1;
        this.rate = 1.0f;
    }

    public int getSoundId() { return this.soundId; }

    public int getLoop() { return this.loop; }

    public void setLoop(int loop) { this.loop = loop; }

    public int getPriority() { return this.priority; }

    public void setPriority(int priority) { this.priority = priority; }

    public float getRate() { return this.rate; }

    public void setRate(float rate) { this.rate = rate; }

    public float getVolume() { return this.volume; }

    public void setVolume(float v) { this.volume = v; }
}

package com.example.engine_android;

import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;

import com.example.engine_common.interfaces.ISound;

public class SoundAndroid implements ISound {
    private float volume;
    private int soundId;
    private int loop;
    private int priority;
    private float rate;

    public SoundAndroid(int soundId_, float volume_, int loop_, int priority_, int rate_) {
        soundId = soundId_;
        volume = volume_;
        loop = loop_;
        priority = priority_;
        rate = rate_;
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

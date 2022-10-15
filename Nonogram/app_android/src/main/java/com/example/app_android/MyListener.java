package com.example.app_android;

import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

public class MyListener implements View.OnTouchListener {
    private SoundPool soundPool;
    private int soundId;

    public void setSoundPool(SoundPool s) {
            soundPool = s;
    }

    public void setSoundId(int Id) {
        soundId = Id;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            System.out.println("hi");
            soundPool.play(soundId, 1,1,
                    0, 0, 1);
            return true;
        }
        return false;
    }
}

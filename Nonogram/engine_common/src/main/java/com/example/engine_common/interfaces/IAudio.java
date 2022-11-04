package com.example.engine_common.interfaces;

import java.io.IOException;

public interface IAudio {

    public void setBackgroundMusic(String filePath) throws IOException;
    public void startBGMusic();
    public void stopBGMusic();
    public void pauseBGMusic();
    public void setBGVolume(float volume);
    public void setVFX(float volume);
    String loadSound(String filePath) throws IOException;
    void playSound(String soundName);
}

package com.example.engine_common.interfaces;

import java.io.IOException;

public interface IAudio {

    public void setMusic(String filePath) throws IOException;
    public void startMusic();
    public void stopMusic();
    public void pauseMusic();
    public void setMusicVolume(float volume);
    public void setSoundVolume(float volume);
    String loadSound(String filePath) throws IOException;
    void playSound(String soundName);
}

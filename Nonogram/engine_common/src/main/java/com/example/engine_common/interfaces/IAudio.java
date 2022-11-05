package com.example.engine_common.interfaces;

import java.io.IOException;

public interface IAudio {
    public void loadMusic(String filePath, float volume);

    public String loadSound(String filePath, float volume);

    public void playMusic();
    public void playSound(String soundName);

    public void setMusicVolume(float volume);
    public void setSoundVolume(String soundName, float volume);
}

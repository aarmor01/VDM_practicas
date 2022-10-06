package com.example.engine_interfaces;

public interface ISound {
    public int getVolume();
    public String getPath();

    public void play();
    public void stop();
    public void setVolume(int v);
}

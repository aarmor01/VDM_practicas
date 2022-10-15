package com.example.engine_interfaces;

public interface IEngine {
    IRender getRender();
    IAudio getAudio();
    IInput getInput();
    IScene getScene();
}

package com.example.engine_common;

public interface IEngine {
    IRender getRender();
    IAudio getAudio();
    IInput getInput();
    IScene getScene();
}

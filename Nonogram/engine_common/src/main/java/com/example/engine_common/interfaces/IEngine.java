package com.example.engine_common.interfaces;

import com.example.engine_common.shared.SceneManager;

public interface IEngine {
    IRender getRender();
    IAudio getAudio();
    SceneManager getSceneManager();
}

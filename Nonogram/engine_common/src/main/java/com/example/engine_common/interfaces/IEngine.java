package com.example.engine_common.interfaces;

import com.example.engine_common.shared.SceneManager;
import com.example.engine_common.shared.InputManager;

public interface IEngine {
    IRender getRender();
    IAudio getAudio();

    SceneManager getSceneManager();
    InputManager getInputManager();
}

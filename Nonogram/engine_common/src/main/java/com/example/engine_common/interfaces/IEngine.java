package com.example.engine_common.interfaces;

import com.example.engine_common.shared.InputManager;
import com.example.engine_common.shared.SceneManager;

public interface IEngine {
    IRender getRender();
    IAudio getAudio();
    InputManager getInputManager();
    SceneManager getSceneManager();
}

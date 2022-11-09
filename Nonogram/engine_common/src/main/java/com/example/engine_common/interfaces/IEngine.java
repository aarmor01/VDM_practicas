package com.example.engine_common.interfaces;

import com.example.engine_common.shared.SceneManager;
import com.example.engine_common.shared.InputManager;

public interface IEngine {
    public IRender getRender();
    public IAudio getAudio();

    public SceneManager getSceneManager();
    public InputManager getInputManager();
}

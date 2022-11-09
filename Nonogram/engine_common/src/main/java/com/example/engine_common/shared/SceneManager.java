package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IScene;

import java.util.Stack;

public class SceneManager {
    Stack<IScene> scenes;
    IEngine engine;

    public SceneManager(IEngine engine) { this.scenes = new Stack<>(); this.engine = engine; }

    public IScene currentScene() { return this.scenes.peek(); }

    public void pushScene(IScene newScene) {
        newScene.init(engine);
        this.scenes.push(newScene);
    }

    public IScene popScene() { return this.scenes.pop(); }
}

package com.example.engine_android;

import java.util.Stack;

public class SceneManager {
    Stack<IScene> scenes;
    EngineAndroid engine;

    public SceneManager(EngineAndroid engine) { this.scenes = new Stack<>(); this.engine = engine; }

    public IScene currentScene() { return this.scenes.peek(); }

    public void pushScene(IScene newScene) {
        newScene.init(engine);
        this.scenes.push(newScene);
    }

    public IScene popScene() { return this.scenes.pop(); }

    public int getStackSize() { return scenes.size();}
}

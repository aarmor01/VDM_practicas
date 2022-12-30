package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IScene;

import java.util.Stack;

public class SceneManager {
    Stack<IScene> scenes;

    public SceneManager() { this.scenes = new Stack<>(); }

    public IScene currentScene() { return this.scenes.peek(); }

    public void pushScene(IScene newScene) {
        this.scenes.push(newScene);
        this.scenes.peek().init();
    }

    public IScene popScene() { return this.scenes.pop(); }

    public int getStackSize() { return scenes.size();}
}

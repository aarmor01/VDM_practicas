package com.example.engine_android.Modules;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.DataStructures.IScene;

import java.util.Stack;

public class SceneManager {
    Stack<IScene> scenes;

    public SceneManager() { this.scenes = new Stack<>(); }

    public IScene currentScene() { return this.scenes.peek(); }

    public void pushScene(IScene newScene, EngineAndroid eng) {
        newScene.init(eng);
        this.scenes.push(newScene);
    }

    public IScene popScene() { return this.scenes.pop(); }

    public boolean isEmpty(){return this.scenes.isEmpty();}
}

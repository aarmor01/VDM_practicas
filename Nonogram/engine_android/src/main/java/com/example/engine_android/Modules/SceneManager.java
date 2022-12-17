package com.example.engine_android.Modules;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.DataStructures.IScene;

import java.util.Stack;

public class SceneManager {
    IScene scene;

    public SceneManager() { this.scene = null; }

    public IScene currentScene() { return this.scene; }

//    public void pushScene(IScene newScene, EngineAndroid eng) {
//        newScene.init(eng);
//        this.scenes.push(newScene);
//    }
//
//    public IScene popScene() { return this.scenes.pop(); }

    public boolean isEmpty(){return this.scene == null; }

    public void changeScene(IScene newScene, EngineAndroid eng){
        newScene.init(eng);
        this.scene = newScene;
    }
}

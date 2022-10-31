package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;

import java.util.Stack;

public class SceneManager {
    Stack<IScene> scenes;

    public SceneManager() { scenes = new Stack<>(); }

    public IScene currentScene() {
        return scenes.peek();
    }

    public void pushScene(IScene newScene) { scenes.push(newScene); }

    public IScene popScene() { return scenes.pop(); }

    public void update(double deltaTime) {
        scenes.peek().update(deltaTime);
    }

    public void render(IRender renderManager) {
        scenes.peek().render(renderManager);
    }

    public void handleInput(IInput input) {
        scenes.peek().handleInput(input);
    }
}

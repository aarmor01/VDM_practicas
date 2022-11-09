package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;

import java.util.Stack;

public class SceneManager {
    Stack<IScene> scenes;

    public SceneManager() { this.scenes = new Stack<>(); }

    public IScene currentScene() { return this.scenes.peek(); }

    public void pushScene(IScene newScene) { this.scenes.push(newScene); }

    public IScene popScene() { return this.scenes.pop(); }

    public void update(double deltaTime) { this.scenes.peek().update(deltaTime); }

    public void render(IRender renderManager) { this.scenes.peek().render(renderManager); }

    public void handleInput(IInput input) { this.scenes.peek().handleInput(input); }
}

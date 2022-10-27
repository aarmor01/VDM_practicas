package com.example.engine_common;

import java.util.Stack;

public class SceneManager {
    Stack<IScene> scenes;

    public IScene currentScene() {
        return scenes.peek();
    }

    public void pushScene(IScene newScene) {
        scenes.push(newScene);
    }

    public void popScene() {
        scenes.pop();
    }

    public void update() {
        scenes.peek().update(7);
    }

    public void render() {
        scenes.peek().render(null);
    }

    public void handleInput() {
        scenes.peek().handleInput();
    }
}

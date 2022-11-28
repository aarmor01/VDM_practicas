package com.example.engine_android;

import java.io.IOException;

public interface IScene {
    public void init(EngineAndroid engine);
    public void update(double deltaTime);
    public void render(RenderAndroid renderMng);
    public void handleInput(InputAndroid input);
}

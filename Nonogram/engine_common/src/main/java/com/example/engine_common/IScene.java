package com.example.engine_common;

public interface IScene {
    public void init(IEngine eng);
    public void update(double deltaTime);
    public void render(IRender renderMng);
    public void handleInput();
}

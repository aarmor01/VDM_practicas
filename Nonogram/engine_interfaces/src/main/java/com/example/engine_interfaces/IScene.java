package com.example.engine_interfaces;

public interface IScene {
    public void init();
    public void update(double deltaTime);
    public void render(IRender renderMng);
}

package com.example.engine_android.DataStructures;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public interface IScene {
    public String getId();

    public void init(EngineAndroid engRef);
    public void rearrange(EngineAndroid engRef);
    public void update(double deltaTime, EngineAndroid engine);
    public void render(RenderAndroid renderMng);
    public void handleInput(InputAndroid input, EngineAndroid engRef);
}

package com.example.app_android;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.RenderAndroid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PruebaScene implements IScene {

    private EngineAndroid engRef;

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        String bf = engRef.readText("levels/animales/10x10/", "mouse.txt");

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {

    }

    @Override
    public void handleInput(InputAndroid input) {

    }
}

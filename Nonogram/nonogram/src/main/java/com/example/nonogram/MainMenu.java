package com.example.nonogram;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.FontType;
import com.example.engine_common.shared.InputType;

public class MainMenu implements IScene {
    Button playButton;
    String title;
    String font;

    private IEngine engRef;

    @Override
    public void init(IEngine engine) {
        engRef = engine;
        font = font = engine.getRender().loadFont("./assets/fonts/arial.ttf", FontType.DEFAULT, 15);
        playButton = new Button(100, 100, 200, 60, "PLAY", "", font);
        title = "NONOGRAMAS";
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(IRender renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.drawText(50, 50, title);
        playButton.render(renderMng);
    }

    @Override
    public void handleInput(IInput input) {
        if(input.getType() == InputType.TOUCH_UP && playButton.isInBUtton(input.getX(), input.getY())){
            engRef.getSceneManager().pushScene(new SelectionMenu());
        }
    }
}

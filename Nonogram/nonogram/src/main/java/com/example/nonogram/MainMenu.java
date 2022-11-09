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
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX_.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        playButton = new Button((engRef.getRender().getWidth() - engRef.getRender().getWidth()/3 )/2, (int)(engRef.getRender().getHeight() /1.5),
                engRef.getRender().getWidth()/3, engRef.getRender().getHeight()/8, "PLAY", "", fontButton);
        title = "NONOGRAMAS";

        engRef.getAudio().loadMusic("./assets/sounds/puzzleTheme.wav", 0.1f);
        engRef.getAudio().playMusic();
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(IRender renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int wi = engRef.getRender().getTextWidth(font, title);
        renderMng.drawText((engRef.getRender().getWidth() - wi)/2, engRef.getRender().getHeight()/6, title);
        playButton.render(renderMng);
    }

    @Override
    public void handleInput(IInput input) {
        if(input.getType() == InputType.TOUCH_UP && playButton.isInBUtton(input.getX(), input.getY())){
            engRef.getSceneManager().pushScene(new SelectionMenu());
        }
    }
}
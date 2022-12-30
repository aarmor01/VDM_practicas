package com.example.nonogram;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.shared.FontType;

public class BootScene implements IScene {
    IEngine engine;

    public BootScene(IEngine engine) { this.engine = engine; }

    @Override
    public void init() {
        // images
        Resources.IMAGE_BACK_BUTTON = this.engine.getRender().loadImage("images/backbutton.png");
        Resources.IMAGE_CHECK_BUTTON = this.engine.getRender().loadImage("images/checkbutton.png");

        // fonts
        int size = Math.min(GameManager.getInstance().getWidth(), GameManager.getInstance().getHeight());
        Resources.FONT_EXO_REGULAR_BIG = this.engine.getRender().loadFont("fonts/Exo-Regular.ttf", FontType.DEFAULT, size / 8);
        Resources.FONT_EXO_REGULAR_MEDIUM = this.engine.getRender().loadFont("fonts/Exo-Regular.ttf", FontType.DEFAULT, size / 10);
        Resources.FONT_KOMIKAX = this.engine.getRender().loadFont("fonts/KOMIKAX.ttf", FontType.DEFAULT, size / 10);
        Resources.FONT_SIMPLY_SQUARE_BIG = this.engine.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, size / 20);
        Resources.FONT_SIMPLY_SQUARE_MEDIUM = this.engine.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, size / 22);

        // sounds
        Resources.SOUND_BUTTON = this.engine.getAudio().loadSound("sounds/button.wav", 1);
        Resources.SOUND_CLICK = this.engine.getAudio().loadSound("sounds/click.wav", 1);

        // music
        this.engine.getAudio().loadMusic("sounds/puzzleTheme.wav", 0.1f);
    }

    @Override
    public void update(double deltaTime, IEngine engine) {
        // change to main menu
        engine.getSceneManager().pushScene(new MainMenu());
    }

    @Override
    public void render(IRender renderer) {}

    @Override
    public void handleInput(IInput input, IEngine engine) {}
}

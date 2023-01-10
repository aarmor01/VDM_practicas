package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Modules.RenderAndroid;

public class BootScene implements IScene {
    @Override
    public String getId() {
        return "BootScene";
    }

    @Override
    public void init(EngineAndroid engine) {
        // images
        Resources.IMAGE_BACK_BUTTON = engine.getRender().loadImage("images/backbutton.png");
        Resources.IMAGE_COIN = engine.getRender().loadImage("images/coin.png");
        Resources.IMAGE_LOCK = engine.getRender().loadImage("images/lock.png");
        Resources.IMAGE_HEART = engine.getRender().loadImage("images/heart.png");
        Resources.IMAGE_NO_HEART = engine.getRender().loadImage("images/no_heart.png");
        Resources.IMAGE_TWITTER_BUTTON = engine.getRender().loadImage("images/twitter_logo.png");
        Resources.IMAGE_RECOVER_HEART = engine.getRender().loadImage("images/recover.png");

        // fonts
        int size = engine.getOrientation()==EngineAndroid.Orientation.PORTRAIT ? GameManager.getInstance().getWidth() : GameManager.getInstance().getHeight();
        Resources.FONT_EXO_REGULAR_BIG = engine.getRender().loadFont("fonts/Exo-Regular.ttf", FontType.DEFAULT, size / 8);
        Resources.FONT_EXO_REGULAR_MEDIUM = engine.getRender().loadFont("fonts/Exo-Regular.ttf", FontType.DEFAULT, size / 10);
        Resources.FONT_KOMIKAX = engine.getRender().loadFont("fonts/KOMIKAX.ttf", FontType.DEFAULT, size / 10);
        Resources.FONT_SIMPLY_SQUARE_BIG = engine.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, size / 20);
        Resources.FONT_SIMPLY_SQUARE_MEDIUM = engine.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, size / 22);

        // sounds
        Resources.SOUND_BUTTON = engine.getAudio().loadSound("sounds/button.wav", 1);
        Resources.SOUND_CLICK = engine.getAudio().loadSound("sounds/click.wav", 1);

        // music
        engine.getAudio().loadMusic("sounds/puzzleTheme.wav", 0.1f);
    }

    @Override
    public void rearrange(EngineAndroid engine) {}

    @Override
    public void update(double deltaTime, EngineAndroid engine) {
        // change to main menu
        engine.getSceneManager().changeScene(new MainMenu(), engine);
    }

    @Override
    public void render(RenderAndroid renderer) {}

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {}
}

package com.example.app_android.Scenes;

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
    public void init(EngineAndroid engRef) {
        // TODO: Load all files here, assign string to Resources final values, then go to MainMenu
        // images
        Resources.IMAGE_BACK_BUTTON = engRef.getRender().loadImage("images/backbutton.png");
        Resources.IMAGE_COIN = engRef.getRender().loadImage("images/coin.png");
        Resources.IMAGE_LOCK = engRef.getRender().loadImage("images/lock.png");
        Resources.IMAGE_HEART = engRef.getRender().loadImage("images/heart.png");
        Resources.IMAGE_NO_HEART = engRef.getRender().loadImage("images/no_heart.png");

        // fonts
        Resources.FONT_EXO_REGULAR_BIG = engRef.getRender().loadFont("fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 8);
        Resources.FONT_EXO_REGULAR_MEDIUM = engRef.getRender().loadFont("fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        Resources.FONT_KOMIKAX = engRef.getRender().loadFont("fonts/KOMIKAX.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        Resources.FONT_SIMPLY_SQUARE_BIG = engRef.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 20);
        Resources.FONT_SIMPLY_SQUARE_MEDIUM = engRef.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);

        // sounds
        Resources.SOUND_BUTTON = engRef.getAudio().loadSound("sounds/button.wav", 1);
        Resources.SOUND_CLICK = engRef.getAudio().loadSound("sounds/click.wav", 1);

        // music
        engRef.getAudio().loadMusic("sounds/puzzleTheme.wav", 0.1f);

        // change to main menu
        engRef.getSceneManager().changeScene(new MainMenu(), engRef);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {}

    @Override
    public void update(double deltaTime, EngineAndroid engine) {}

    @Override
    public void render(RenderAndroid renderMng) {}

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {}
}

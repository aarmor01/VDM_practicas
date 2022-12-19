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
        Resources.Id1 = engRef.getRender().loadImage("./assets/images/backbutton.png");
        Resources.Id2 = engRef.getRender().loadImage("./assets/images/coin.png");
        Resources.Id3 = engRef.getRender().loadImage("./assets/images/lock.png");
        Resources.Id4 = engRef.getRender().loadImage("./assets/images/heart.png");
        Resources.Id5 = engRef.getRender().loadImage("./assets/images/no_heart.png");

        // fonts
        Resources.Id6 = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 8);
        Resources.Id7 = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);

        Resources.Id8 = engRef.getRender().loadFont("./assets/fonts/KOMIKAX.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);

        Resources.Id9 = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);

        // sounds
        Resources.Id10 = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        Resources.Id11 = engRef.getAudio().loadSound("./assets/sounds/click.wav", 1);

        // music
        engRef.getAudio().loadMusic("./assets/sounds/puzzleTheme.wav", 0.1f);

        engRef.getSceneManager().changeScene(new MainMenu(), engRef);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {}

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(RenderAndroid renderMng) {}

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {}
}

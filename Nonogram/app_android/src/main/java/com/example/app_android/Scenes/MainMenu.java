package com.example.app_android.Scenes;

import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class MainMenu implements IScene {
    Button playButton;
    String title;
    String font;

    @Override
    public String getId() {
        return "MainMenu";
    }

    //private EngineAndroid engRef;
    @Override
    public void init(EngineAndroid engRef) {
        if (engRef.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait(engRef);
        else if (engRef.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape(engRef);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {
        if (engRef.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait(engRef);
        else if (engRef.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape(engRef);
    }

    @Override
    public void update(double deltaTime) {
//        if(engRef.getAdSystem().hasRewardBeenGranted())
//            System.out.println("Reward Granted on " + getId());
    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int wi = renderMng.getTextWidth(font, title);
        renderMng.drawText((renderMng.getWidth() - wi) / 2, renderMng.getHeight() / 6, title);
        playButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engRef) {
        if (input.getType() == InputType.TOUCH_UP && playButton.isInButton(input.getX(), input.getY())) {
            engRef.getSceneManager().changeScene(new ModeSelectionMenu(), engRef);
            playButton.clicked(engRef.getAudio());
        }
    }

    private void arrangePortrait(EngineAndroid engRef) {
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);

        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        playButton = new Button((engRef.getRender().getWidth() - engRef.getRender().getWidth() / 3) / 2, (int) (engRef.getRender().getHeight() / 1.5),
                engRef.getRender().getWidth() / 3, engRef.getRender().getHeight() / 8, "PLAY", "", fontButton, btAudio, false);
        title = "NONOGRAMAS";

        engRef.getAudio().loadMusic("./assets/sounds/puzzleTheme.wav", 0.1f);
        engRef.getAudio().playMusic();
    }

    private void arrangeLandscape(EngineAndroid engRef) {
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);

        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        playButton = new Button((engRef.getRender().getWidth() - engRef.getRender().getWidth() / 3) / 2, (int) (engRef.getRender().getHeight() / 1.5),
                engRef.getRender().getWidth() / 3, engRef.getRender().getHeight() / 8, "PLAY", "", fontButton, btAudio, false);
        title = "NANOGRAMO";

        engRef.getAudio().loadMusic("./assets/sounds/puzzleTheme.wav", 0.1f);
        engRef.getAudio().playMusic();
    }
}
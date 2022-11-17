package com.example.app_android;


import com.example.engine_android.EngineAndroid;
import com.example.engine_android.FontType;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.InputType;
import com.example.engine_android.RenderAndroid;

public class MainMenu implements IScene {
    Button playButton;
    String title;
    String font;

    private EngineAndroid engRef;
    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX_.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);

        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        playButton = new Button((engRef.getRender().getWidth() - engRef.getRender().getWidth()/3 )/2, (int)(engRef.getRender().getHeight() /1.5),
                engRef.getRender().getWidth()/3, engRef.getRender().getHeight()/8, "PLAY", "", fontButton, btAudio);
        title = "NONOGRAMAS";

        engRef.getAudio().loadMusic("./assets/sounds/puzzleTheme.wav", 0.1f);
        engRef.getAudio().playMusic();
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int wi = engRef.getRender().getTextWidth(font, title);
        renderMng.drawText((engRef.getRender().getWidth() - wi)/2, engRef.getRender().getHeight()/6, title);
        playButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        if(input.getType() == InputType.TOUCH_UP && playButton.isInButton(input.getX(), input.getY())){
            engRef.getSceneManager().pushScene(new SelectionMenu());
            playButton.clicked(engRef.getAudio());
        }
    }
}
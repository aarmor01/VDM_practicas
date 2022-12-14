package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;

import com.example.app_android.R;
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

    private EngineAndroid engRef;
    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;

        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, GameManager.getInstance().getWidth() / 10);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX_.ttf", FontType.DEFAULT, GameManager.getInstance().getWidth() / 10);

        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        playButton = new Button((GameManager.getInstance().getWidth() - GameManager.getInstance().getWidth()/3 )/2, (int)(GameManager.getInstance().getHeight() /1.5),
                GameManager.getInstance().getWidth()/3, GameManager.getInstance().getHeight()/8, "PLAY", "", fontButton, btAudio, false);
        title = "NONOGRAMAS";

        engRef.getAudio().loadMusic("./assets/sounds/puzzleTheme.wav", 0.1f);
        engRef.getAudio().playMusic();
    }

    @Override
    public String getId() { return "MainMenu"; }


    @Override
    public void update(double deltaTime) {
        if(engRef.getAdSystem().hasRewardBeenGranted())
            System.out.println("Reward Granted on " + getId());
    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int wi = renderMng.getTextWidth(font, title);
        renderMng.drawText((GameManager.getInstance().getWidth() - wi)/2, GameManager.getInstance().getHeight()/6, title);
        playButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        if(input.getType() == InputType.TOUCH_UP && playButton.isInButton(input.getX(), input.getY())){
            engRef.getSceneManager().pushScene(new ModeSelectionMenu());
            engRef.getIntentSystemAndroid().createNotification(androidx.constraintlayout.widget.R.drawable.notification_template_icon_low_bg);
            playButton.clicked(engRef.getAudio());
        }
    }
}
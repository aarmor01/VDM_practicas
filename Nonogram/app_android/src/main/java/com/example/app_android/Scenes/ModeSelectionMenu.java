package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class ModeSelectionMenu implements IScene {
    private EngineAndroid engRef;
    private String mainText;
    private String font;
    private Button playRandomLevelButton;
    private Button playThemeButton;
    private Button coinIndicator;

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, GameManager.getInstance().getWidth() / 10);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX_.ttf", FontType.DEFAULT, GameManager.getInstance().getWidth() / 10);
        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();

        playRandomLevelButton = new Button(0, (int)(getH /1.25),
                getW, getH/8, "RANDOM LEVELS", "", fontButton, btAudio, false);
        playThemeButton = new Button(0, (int)(getH /1.75),
                getW, getH/8, "THEME LEVELS", "", fontButton, btAudio, false);
        coinIndicator = new Button(5* getW/8, 0, getW/4, getW/8, Integer.toString(GameManager.getInstance().getCoins()),
                engRef.getRender().loadImage("./assets/images/coin.png"), fontButton, "", false);
        mainText = "Chose play mode:";
    }

    @Override
    public String getId(){return "ModeSelectionMenu";}

    @Override
    public void update(double deltaTime) {
        coinIndicator.setText(Integer.toString(GameManager.getInstance().getCoins()));
    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int wi = engRef.getRender().getTextWidth(font, mainText);
        renderMng.drawText((GameManager.getInstance().getWidth() - wi)/2, GameManager.getInstance().getHeight()/6, mainText);
        playRandomLevelButton.render(renderMng);
        playThemeButton.render(renderMng);
        coinIndicator.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        if(input.getType() == InputType.TOUCH_UP || input.getType() == InputType.TOUCH_LONG) {
            if (playRandomLevelButton.isInButton(input.getX(), input.getY())){
                engRef.getSceneManager().pushScene(new SelectionMenu());
                playRandomLevelButton.clicked(engRef.getAudio());
            }
            else if (playThemeButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().pushScene(new ThemeSelectionMenu());
                playThemeButton.clicked(engRef.getAudio());
            }
        }
    }
}

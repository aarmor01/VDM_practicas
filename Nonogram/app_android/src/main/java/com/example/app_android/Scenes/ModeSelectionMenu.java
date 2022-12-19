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
    //private EngineAndroid engRef;
    private String mainText;
    private String font;
    private Button playRandomLevelButton;
    private Button playThemeButton;
    private Button coinIndicator;

    @Override
    public void init(EngineAndroid engRef) {
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        int getW = engRef.getRender().getWidth();
        int getH = engRef.getRender().getHeight();

        playRandomLevelButton = new Button(0, (int)(getH /1.25),
                getW, getH/8, "RANDOM LEVELS", "", fontButton, btAudio, false);
        playThemeButton = new Button(0, (int)(getH /1.75),
                getW, getH/8, "THEME LEVELS", "", fontButton, btAudio, false);
        coinIndicator = new Button(5* getW/8, 0, getW/4, getW/8, Integer.toString(GameManager.getInstance().getCoins()),
                engRef.getRender().loadImage("./assets/images/coin.png"), fontButton, "", false);
        mainText = "Chose play mode:";
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

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
        int wi = renderMng.getTextWidth(font, mainText);
        renderMng.drawText((renderMng.getWidth() - wi)/2, renderMng.getHeight()/6, mainText);
        playRandomLevelButton.render(renderMng);
        playThemeButton.render(renderMng);
        coinIndicator.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if(input.getType() == InputType.TOUCH_UP || input.getType() == InputType.TOUCH_LONG) {
            if (playRandomLevelButton.isInButton(input.getX(), input.getY())){
                engine.getSceneManager().changeScene(new SelectionMenu(), engine);
                playRandomLevelButton.clicked(engine.getAudio());
            }
            else if (playThemeButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new ThemeSelectionMenu(), engine);
                playThemeButton.clicked(engine.getAudio());
            }
        }
    }
}

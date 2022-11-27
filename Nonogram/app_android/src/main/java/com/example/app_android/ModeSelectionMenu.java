package com.example.app_android;


import com.example.engine_android.EngineAndroid;
import com.example.engine_android.FontType;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.InputType;
import com.example.engine_android.RenderAndroid;

public class ModeSelectionMenu implements IScene {
    private EngineAndroid engRef;
    private String mainText;
    private String font;
    private Button playRandomLevelButton;
    private Button playThemeButton;

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX_.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        playRandomLevelButton = new Button((engRef.getRender().getWidth() - engRef.getRender().getWidth() )/2, (int)(engRef.getRender().getHeight() /1.25),
                engRef.getRender().getWidth(), engRef.getRender().getHeight()/8, "RANDOM LEVELS", "", fontButton, btAudio);
        playThemeButton = new Button((engRef.getRender().getWidth() - engRef.getRender().getWidth() )/2, (int)(engRef.getRender().getHeight() /1.75),
                engRef.getRender().getWidth(), engRef.getRender().getHeight()/8, "THEME LEVELS", "", fontButton, btAudio);
        mainText = "Chose play mode:";
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int wi = engRef.getRender().getTextWidth(font, mainText);
        renderMng.drawText((engRef.getRender().getWidth() - wi)/2, engRef.getRender().getHeight()/6, mainText);
        playRandomLevelButton.render(renderMng);
        playThemeButton.render(renderMng);
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

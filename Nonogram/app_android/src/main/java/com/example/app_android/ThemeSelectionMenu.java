package com.example.app_android;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.FontType;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.InputType;
import com.example.engine_android.RenderAndroid;

public class ThemeSelectionMenu implements IScene {
    private Button animalThemeButton;
    private String mainText;
    private String font;
    private String backFont;
    Button backButton;

    private EngineAndroid engRef;

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        String fontButton = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        font = engRef.getRender().loadFont("./assets/fonts/KOMIKAX_.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 10);
        backFont = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        animalThemeButton = new Button((engRef.getRender().getWidth() - engRef.getRender().getWidth())/2, (int)(engRef.getRender().getHeight() /1.5),
                engRef.getRender().getWidth(), engRef.getRender().getHeight()/8, "ANIMAL THEME", "", fontButton, btAudio);
        mainText = "Chose theme:";
        backButton = new Button(engRef.getRender().getWidth()/3, (engRef.getRender().getHeight()/6)*5, engRef.getRender().getWidth()/3, (engRef.getRender().getHeight()/6)/3, "Back", "", backFont, btAudio);

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
        animalThemeButton.render(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        if(input.getType() == InputType.TOUCH_UP){
            if (animalThemeButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/animales/", 0));
                animalThemeButton.clicked(engRef.getAudio());
            }
            else if(backButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().popScene();
                backButton.clicked(engRef.getAudio());
            }
        }
    }
}

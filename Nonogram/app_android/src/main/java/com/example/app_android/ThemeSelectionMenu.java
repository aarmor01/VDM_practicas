package com.example.app_android;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.FontType;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.InputType;
import com.example.engine_android.RenderAndroid;

public class ThemeSelectionMenu implements IScene {
    private Button animalThemeButton;
    private Button emojiThemeButton;
    private Button theme3ThemeButton;
    private Button theme4ThemeButton;
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
        animalThemeButton = new Button(0, (int)(engRef.getRender().getHeight() / 4.0),
                engRef.getRender().getWidth(), engRef.getRender().getHeight()/10, "ANIMAL THEME", "", fontButton, btAudio);
        emojiThemeButton = new Button(0, (int)(engRef.getRender().getHeight() / 2.75),
                engRef.getRender().getWidth(), engRef.getRender().getHeight()/10, "EMOJI THEME", "", fontButton, btAudio);
        theme3ThemeButton = new Button(0, (int)(engRef.getRender().getHeight() / 2.1),
                engRef.getRender().getWidth(), engRef.getRender().getHeight()/10, "SOON", "", fontButton, btAudio);
        theme4ThemeButton = new Button(0, (int)(engRef.getRender().getHeight() / 1.7),
                engRef.getRender().getWidth(), engRef.getRender().getHeight()/10, "SOON", "", fontButton, btAudio);
        mainText = "Choose theme:";
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
        emojiThemeButton.render(renderMng);
        theme3ThemeButton.render(renderMng);
        theme4ThemeButton.render(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        GameManager gM = GameManager.getInstance();
        if(input.getType() == InputType.TOUCH_UP){
            if (animalThemeButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/animales/", 0));
                animalThemeButton.clicked(engRef.getAudio());
            }
            else if (emojiThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(0) > 10) {
                if(gM.getLevelUnlocked(1) == -1) gM.updateCategory(1, 0, null);
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/emojis/", 1));
                emojiThemeButton.clicked(engRef.getAudio());
            }
            else if (theme3ThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(1) > 21) {
                if(gM.getLevelUnlocked(2) == -1) gM.updateCategory(2, 0, null);
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/theme3/", 2));
                emojiThemeButton.clicked(engRef.getAudio());
            }
            else if (theme4ThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(2) > 21) {
                if(gM.getLevelUnlocked(3) == -1) gM.updateCategory(3, 0, null);
                engRef.getSceneManager().pushScene(new LevelHistorySelectionMenu("levels/theme4/", 3));
                emojiThemeButton.clicked(engRef.getAudio());
            }
            else if(backButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().popScene();
                backButton.clicked(engRef.getAudio());
            }
        }
    }
}

package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class ThemeSelectionMenu implements IScene {
    private String mainText;
    private String mainFont;

    private Button animalThemeButton;
    private Button emojiThemeButton;
    private Button theme3ThemeButton;
    private Button theme4ThemeButton;

    private Button backButton;
    private Button coinIndicator;

    @Override
    public String getId() {
        return "ThemeSelectionMenu";
    }

    @Override
    public void init(EngineAndroid engRef) {
        this.mainText = "Choose theme:";
        this.mainFont = Resources.FONT_KOMIKAX;

        this.animalThemeButton = new Button(0, (int) (engRef.getRender().getHeight() / 4.0),
                engRef.getRender().getWidth(), engRef.getRender().getHeight() / 10, "ANIMAL THEME", "",
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);

        String buttonImage = GameManager.getInstance().getLevelUnlocked(1) > 10 ? "" : Resources.IMAGE_LOCK;
        this.emojiThemeButton = new Button(0, (int) (engRef.getRender().getHeight() / 2.75),
                engRef.getRender().getWidth(), engRef.getRender().getHeight() / 10, "EMOJI THEME", buttonImage,
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);

        buttonImage = GameManager.getInstance().getLevelUnlocked(2) > 10 ? "" : Resources.IMAGE_LOCK;
        this.theme3ThemeButton = new Button(0, (int) (engRef.getRender().getHeight() / 2.1), engRef.getRender().getWidth(),
                engRef.getRender().getHeight() / 10, "SOON", buttonImage,
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);

        buttonImage = GameManager.getInstance().getLevelUnlocked(3) > 10 ? "" : Resources.IMAGE_LOCK;
        this.theme4ThemeButton = new Button(0, (int) (engRef.getRender().getHeight() / 1.7), engRef.getRender().getWidth(),
                engRef.getRender().getHeight() / 10, "SOON", buttonImage,
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);

        this.backButton = new Button(engRef.getRender().getWidth() / 3, (engRef.getRender().getHeight() / 6) * 5,
                engRef.getRender().getWidth() / 3, (engRef.getRender().getHeight() / 6) / 3, "Back",
                "", Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);

        this.coinIndicator = new Button(5 * engRef.getRender().getWidth() / 8, 0, engRef.getRender().getWidth() / 4,
                engRef.getRender().getWidth() / 8, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_EXO_REGULAR_MEDIUM, "", false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime) {
        
    }

    @Override
    public void render(RenderAndroid renderMng) {
        // render text
        renderMng.setColor(0xFF000000);
        renderMng.setFont(this.mainFont);
        int textWidth = renderMng.getTextWidth(this.mainFont, this.mainText);
        renderMng.drawText((renderMng.getWidth() - textWidth) / 2, renderMng.getHeight() / 6, this.mainText);

        // buttons
        this.animalThemeButton.render(renderMng);
        this.emojiThemeButton.render(renderMng);
        this.theme3ThemeButton.render(renderMng);
        this.theme4ThemeButton.render(renderMng);

        this.coinIndicator.render(renderMng);
        this.backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engRef) {
        GameManager gM = GameManager.getInstance();
        if (input.getType() == InputType.TOUCH_UP) {
            if (this.animalThemeButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new LevelHistorySelectionMenu(1), engRef);
                this.animalThemeButton.clicked(engRef.getAudio());
            } else if (this.emojiThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(1) > 0) {
                if (gM.getLevelUnlocked(2) == -1) {
                    gM.updateCategory(2, 0, null);
                }
                engRef.getSceneManager().changeScene(new LevelHistorySelectionMenu(2), engRef);
                this.emojiThemeButton.clicked(engRef.getAudio());
            } else if (this.theme3ThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(2) > 20) {
                if (gM.getLevelUnlocked(3) == -1) {
                    gM.updateCategory(3, 0, null);
                }
                engRef.getSceneManager().changeScene(new LevelHistorySelectionMenu(3), engRef);
                this.emojiThemeButton.clicked(engRef.getAudio());
            } else if (this.theme4ThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(3) > 20) {
                if (gM.getLevelUnlocked(4) == -1) {
                    gM.updateCategory(4, 0, null);
                }
                engRef.getSceneManager().changeScene(new LevelHistorySelectionMenu(4), engRef);
                this.emojiThemeButton.clicked(engRef.getAudio());
            } else if (this.backButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new ModeSelectionMenu(), engRef);
                this.backButton.clicked(engRef.getAudio());
            }
        }
    }
}

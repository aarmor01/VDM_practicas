package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Label;
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
    private Label mainLabel;

    private Button animalThemeButton;
    private Button emojiThemeButton;
    private Button kitchenThemeButton;
    private Button christmasThemeButton;

    private Button backButton;
    private Button coinIndicator;

    final int LEVEL_UNLOCK_CATEGORY = 1;

    @Override
    public String getId() {
        return "ThemeSelectionMenu";
    }

    @Override
    public void init(EngineAndroid engine) {
        this.mainLabel = new Label(0, 0, "Choose theme", Resources.FONT_KOMIKAX);

        this.animalThemeButton = new Button(0, 0, 0, 0, "ANIMAL THEME", "",
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON);
        String buttonImage = GameManager.getInstance().getLevelUnlocked(1) > LEVEL_UNLOCK_CATEGORY ? "" : Resources.IMAGE_LOCK;
        this.emojiThemeButton = new Button(0, 0, 0, 0, "EMOJI THEME", buttonImage,
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON);
        buttonImage = GameManager.getInstance().getLevelUnlocked(2) > LEVEL_UNLOCK_CATEGORY ? "" : Resources.IMAGE_LOCK;
        this.kitchenThemeButton = new Button(0, 0, 0, 0, "KITCHEN THEME", buttonImage,
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON);
        buttonImage = GameManager.getInstance().getLevelUnlocked(3) > LEVEL_UNLOCK_CATEGORY ? "" : Resources.IMAGE_LOCK;
        this.christmasThemeButton = new Button(0, 0, 0, 0, "XMAS THEME", buttonImage,
                Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON);
        this.backButton = new Button(0, 0, 0, 0, "Back",
                "", Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.coinIndicator = new Button(0, 0, 0, 0,  Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE_MEDIUM, "");

        rearrange(engine);
    }

    @Override
    public void rearrange(EngineAndroid engine) {
        if (engine.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait();
        else if (engine.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape();
    }

    @Override
    public void update(double deltaTime, EngineAndroid engine) {
        
    }

    @Override
    public void render(RenderAndroid renderer) {
        // render text
        renderer.setColor(0xFF000000);

        // label
        this.mainLabel.render(renderer);

        // buttons
        this.animalThemeButton.render(renderer);
        this.emojiThemeButton.render(renderer);
        this.kitchenThemeButton.render(renderer);
        this.christmasThemeButton.render(renderer);

        this.coinIndicator.render(renderer);
        this.backButton.render(renderer);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        GameManager gM = GameManager.getInstance();
        if (input.getType() == InputType.TOUCH_UP) {
            if (this.animalThemeButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new CategoryLevelSelectionMenu(1), engine);
                this.animalThemeButton.clicked(engine.getAudio());
            } else if (this.emojiThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(1) > LEVEL_UNLOCK_CATEGORY) {
                if (gM.getLevelUnlocked(2) == -1) {
                    gM.updateCategory(2, 0, null, -1);
                }
                engine.getSceneManager().changeScene(new CategoryLevelSelectionMenu(2), engine);
                this.emojiThemeButton.clicked(engine.getAudio());
            } else if (this.kitchenThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(2) > LEVEL_UNLOCK_CATEGORY) {
                if (gM.getLevelUnlocked(3) == -1) {
                    gM.updateCategory(3, 0, null, -1);
                }
                engine.getSceneManager().changeScene(new CategoryLevelSelectionMenu(3), engine);
                this.emojiThemeButton.clicked(engine.getAudio());
            } else if (this.christmasThemeButton.isInButton(input.getX(), input.getY()) && gM.getLevelUnlocked(3) > LEVEL_UNLOCK_CATEGORY) {
                if (gM.getLevelUnlocked(4) == -1) {
                    gM.updateCategory(4, 0, null, -1);
                }
                engine.getSceneManager().changeScene(new CategoryLevelSelectionMenu(4), engine);
                this.emojiThemeButton.clicked(engine.getAudio());
            } else if (this.backButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new ModeSelectionMenu(), engine);
                this.backButton.clicked(engine.getAudio());
            }
        }
    }

    private void arrangePortrait() {
        // label
        this.mainLabel.setPos(GameManager.getInstance().getWidth() / 2, GameManager.getInstance().getHeight() / 6);

        // buttons
        int w = GameManager.getInstance().getWidth();
        int h = GameManager.getInstance().getHeight();
        this.animalThemeButton.setPosition(0, (int) (h / 4.0));
        this.animalThemeButton.setSize(w, h / 10);
        this.emojiThemeButton.setPosition(0, (int) (h / 2.75));
        this.emojiThemeButton.setSize(w, h / 10);
        this.kitchenThemeButton.setPosition(0, (int) (h / 2.1));
        this.kitchenThemeButton.setSize(w, h / 10);
        this.christmasThemeButton.setPosition(0, (int) (h / 1.7));
        this.christmasThemeButton.setSize(w, h / 10);

        this.backButton.setPosition(w / 3, (h / 6) * 5);
        this.backButton.setSize(w / 3, (h / 6) / 3);
        this.coinIndicator.setPosition(5 * w / 8, 0);
        this.coinIndicator.setSize(w / 4, w / 8);
    }

    private void arrangeLandscape() {
        // label
        this.mainLabel.setPos(GameManager.getInstance().getWidth() / 2, GameManager.getInstance().getHeight() / 6);

        // buttons
        int w = GameManager.getInstance().getWidth();
        int h = GameManager.getInstance().getHeight();
        this.animalThemeButton.setPosition(0, (int) (h / 4.0));
        this.animalThemeButton.setSize(w, h / 10);
        this.emojiThemeButton.setPosition(0, (int) (h / 2.75));
        this.emojiThemeButton.setSize(w, h / 10);
        this.kitchenThemeButton.setPosition(0, (int) (h / 2.1));
        this.kitchenThemeButton.setSize(w, h / 10);
        this.christmasThemeButton.setPosition(0, (int) (h / 1.7));
        this.christmasThemeButton.setSize(w, h / 10);

        this.backButton.setPosition(w / 3, (h / 6) * 5);
        this.backButton.setSize(w / 3, (h / 6) / 3);
        this.coinIndicator.setPosition(w - w / 5, 0);
        this.coinIndicator.setSize(w / 5, h / 8);
    }
}

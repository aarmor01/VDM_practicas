package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class CategoryLevelSelectionMenu implements IScene {
    private final int LEVELS_PER_CATEGORY = 20;

    private Button[] levelSelectionButtons;
    private Button backButton;
    private Button coinIndicator;

    private String path;
    private int lastUnlocked, category;

    public CategoryLevelSelectionMenu(int cat) {
        switch (cat) {
            case 1:
                this.path = "levels/animales/";
                break;
            case 2:
                this.path = "levels/emojis/";
                break;
            case 3:
                this.path = "levels/cocina/";
                break;
            case 4:
                this.path = "levels/navidad/";
                break;
        }
        this.category = cat;
        this.lastUnlocked = GameManager.getInstance().getLevelUnlocked(this.category) + 1;
    }

    @Override
    public String getId() {
        return "LevelHistorySelectionMenu";
    }

    @Override
    public void init(EngineAndroid engRef) {
        // selection levels
        this.levelSelectionButtons = new Button[LEVELS_PER_CATEGORY];
        int x = GameManager.getInstance().getWidth() / 5;
        int y = GameManager.getInstance().getHeight() / 4;
        for (int i = 0; i < LEVELS_PER_CATEGORY; i++) {
            String image = i >= this.lastUnlocked ? Resources.IMAGE_LOCK : "";
            String text = i < this.lastUnlocked ? "Lvl" + (i + 1) : "";
            this.levelSelectionButtons[i] = new Button(0, 0, 0, 0,  text, image,
                    Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        }

        // buttons
        int getW = GameManager.getInstance().getWidth();
        this.backButton = new Button(0, 0, 0, 0, "Back",
                Resources.IMAGE_BACK_BUTTON, Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.coinIndicator = new Button(0, 0, 0, 0, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE_MEDIUM, "", false);

        rearrange(engRef);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {
        if (engRef.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait();
        else if (engRef.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape();
    }

    @Override
    public void update(double deltaTime, EngineAndroid engine) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        // buttons
        renderMng.setColor(0xFF000000);
        for (int i = 0; i < LEVELS_PER_CATEGORY; i++)
            this.levelSelectionButtons[i].render(renderMng);
        this.backButton.render(renderMng);
        this.coinIndicator.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engRef) {
        if (input.getType() == InputType.TOUCH_UP) {
            // back
            if (this.backButton.isInButton(input.getX(), input.getY())) {
                this.backButton.clicked(engRef.getAudio());
                engRef.getSceneManager().changeScene(new ThemeSelectionMenu(), engRef);
            }

            // check for level selected
            int level = 0;
            while (level < this.lastUnlocked) {
                if (this.levelSelectionButtons[level].isInButton(input.getX(), input.getY()))
                    break;
                level++;
            }

            // if level selected
            if (level < this.lastUnlocked) {
                this.levelSelectionButtons[0].clicked(engRef.getAudio());
                engRef.getSceneManager().changeScene(new BoardScene(this.path, level + 1, this.category), engRef);
            }
        }
    }

    private void arrangePortrait() {
        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.coinIndicator.setPosition(5 * getW / 8, 0);
        this.coinIndicator.setSize(getW / 3, getW / 8);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.backButton.setPosition(getW / 3, 7 * getH / 8);
        this.backButton.setSize(getW / 3, getW / 8);
        this.backButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        int x = getW / 5;
        int y = getH / 8;
        for (int i = 0; i < LEVELS_PER_CATEGORY; i++) {
            this.levelSelectionButtons[i].setPosition(x / 2 + x * (i % 4), y + (y * (i / 4)));
            this.levelSelectionButtons[i].setSize(x - x / 10, x - x / 10);
        }
    }

    private void arrangeLandscape() {
        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.coinIndicator.setPosition(5 * getW / 8, 0);
        this.coinIndicator.setSize(getW / 3, getW / 8);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.backButton.setPosition(getW / 3, 7 * getH / 8);
        this.backButton.setSize(getW / 3, getW / 8);
        this.backButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        int x = getW / 5;
        int y = getH / 8;
        for (int i = 0; i < LEVELS_PER_CATEGORY; i++) {
            this.levelSelectionButtons[i].setPosition(x / 2 + x * (i % 4), y + (y * (i / 4)));
            this.levelSelectionButtons[i].setSize(x - x / 10, x - x / 10);
        }
    }
}
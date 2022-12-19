package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class LevelHistorySelectionMenu implements IScene {
    private final int LEVELS_PER_CATEGORY = 20;

    private Button[] levelSelectionButtons;
    private Button backButton;
    private Button coinIndicator;

    private String path;
    private int lastUnlocked, category;

    public LevelHistorySelectionMenu(int cat) {
        switch (cat) {
            case 1:
                this.path = "levels/animales/";
                break;
            case 2:
                this.path = "levels/emojis/";
                break;
            case 3:
                this.path = "levels/theme3/";
                break;
            case 4:
                this.path = "levels/theme4/";
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
        int x = engRef.getRender().getWidth() / 5;
        int y = engRef.getRender().getHeight() / 4;
        for (int i = 0; i < LEVELS_PER_CATEGORY; i++) {
            String image = i >= this.lastUnlocked ? Resources.IMAGE_LOCK : "";
            this.levelSelectionButtons[i] = new Button(x / 2 + x * (i % 4), y + (y / 2 * (i / 4)), x - x / 10, x - x / 10,
                    "Lvl " + (i + 1), image, Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
        }

        // buttons
        int getW = engRef.getRender().getWidth();
        this.backButton = new Button(getW / 8, y / 4, getW / 4, (y - y / 4) / 2, "Back",
                Resources.IMAGE_BACK_BUTTON, Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
        this.coinIndicator = new Button(5 * getW / 8, y / 4, getW / 4, (y - y / 4) / 2,
                Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE, "", false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime) {
//        this.lastUnlocked = GameManager.getInstance().getLevelUnlocked(this.category) + 1;
//        for (int i = 0; i < lastUnlocked; i++)
//            this.levelSelectionButtons[i].setImage("");
//        this.coinIndicator.setText(Integer.toString(GameManager.getInstance().getCoins()));
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
            int x = 0;
            int i = 0;
            while (x == 0 && i < this.lastUnlocked) {
                if (this.levelSelectionButtons[i].isInButton(input.getX(), input.getY()))
                    x = i + 1;
                i++;
            }

            if (this.backButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new ThemeSelectionMenu(), engRef);
                this.backButton.clicked(engRef.getAudio());
            }

            if (x != 0) {
                this.levelSelectionButtons[0].clicked(engRef.getAudio());
                engRef.getSceneManager().changeScene(new BoardScene(this.path, x, this.category), engRef);
            }
        }
    }
}

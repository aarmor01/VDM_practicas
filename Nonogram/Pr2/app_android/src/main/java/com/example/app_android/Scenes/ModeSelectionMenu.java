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

public class ModeSelectionMenu implements IScene {
    private Label mainText;

    private Button playRandomLevelButton;
    private Button playThemeButton;
    private Button paletteMenu;
    private Button coinIndicator;

    @Override
    public String getId() {
        return "ModeSelectionMenu";
    }

    @Override
    public void init(EngineAndroid engine) {
        // main text
        this.mainText = new Label(0, 0, "Select mode", Resources.FONT_KOMIKAX);

        // buttons
        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.playRandomLevelButton = new Button(0, 0, 0, 0,
                "RANDOM LEVELS", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON);
        this.playThemeButton = new Button(0, 0, 0, 0,
                "THEME LEVELS", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON);
        this.paletteMenu = new Button(0, 0, 0, 0,
                "PALETTE SELECTION", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON);
        this.coinIndicator = new Button(0, 0, 0, 0, Integer.toString(GameManager.getInstance().getCoins()),
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
        // text
        this.mainText.render(renderer);

        // buttons
        this.playRandomLevelButton.render(renderer);
        this.playThemeButton.render(renderer);
        this.paletteMenu.render(renderer);
        this.coinIndicator.render(renderer);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if (input.getType() == InputType.TOUCH_UP || input.getType() == InputType.TOUCH_LONG) {
            if (this.playRandomLevelButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new SelectionMenu(), engine);
                this.playRandomLevelButton.clicked(engine.getAudio());
            } else if (this.playThemeButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new ThemeSelectionMenu(), engine);
                this.playThemeButton.clicked(engine.getAudio());
            } else if (this.paletteMenu.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new PaletteMenu(), engine);
                this.paletteMenu.clicked(engine.getAudio());
            }
        }
    }

    private void arrangePortrait() {
        this.mainText.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.playRandomLevelButton.setPosition(0, (int)(getH * 2 / 8));
        this.playRandomLevelButton.setSize(getW, getH / 8);
        this.playRandomLevelButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.playThemeButton.setPosition(0, (int) (getH * 4 / 8));
        this.playThemeButton.setSize(getW, getH / 8);
        this.playThemeButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.paletteMenu.setPosition(0, (int) (getH * 6 / 8));
        this.paletteMenu.setSize(getW, getH / 8);
        this.paletteMenu.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.coinIndicator.setPosition(5 * getW / 8, 0);
        this.coinIndicator.setSize(getW / 4, getW / 8);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
    }

    private void arrangeLandscape() {
        this.mainText.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.playRandomLevelButton.setPosition(0, (int)(getH * 2 / 8));
        this.playRandomLevelButton.setSize(getW, getH / 8);

        this.playThemeButton.setPosition(0, (int) (getH * 4 / 8));
        this.playThemeButton.setSize(getW, getH / 8);

        this.paletteMenu.setPosition(0, (int) (getH * 6 / 8));
        this.paletteMenu.setSize(getW, getH / 8);

        this.coinIndicator.setPosition(getW - getW / 5, 0);
        this.coinIndicator.setSize(getW / 5, getH / 8);
    }
}

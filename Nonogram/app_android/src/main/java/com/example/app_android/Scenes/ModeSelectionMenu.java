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
    public void init(EngineAndroid engRef) {
        // main text
        this.mainText = new Label("Select mode: ", 0, 0, Resources.FONT_KOMIKAX);

        // buttons
        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.playRandomLevelButton = new Button(0, 0, 0, 0,
                "RANDOM LEVELS", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);
        this.playThemeButton = new Button(0, 0, 0, 0,
                "THEME LEVELS", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);
        this.paletteMenu = new Button(0, 0, 0, 0,
                "PALETTES MENU", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);
        this.coinIndicator = new Button(0, 0, 0, 0, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_EXO_REGULAR_MEDIUM, "", false);

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
        // text
        this.mainText.render(renderMng);

        // buttons
        this.playRandomLevelButton.render(renderMng);
        this.playThemeButton.render(renderMng);
        this.paletteMenu.render(renderMng);
        this.coinIndicator.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engRef) {
        if (input.getType() == InputType.TOUCH_UP || input.getType() == InputType.TOUCH_LONG) {
            if (this.playRandomLevelButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new SelectionMenu(), engRef);
                this.playRandomLevelButton.clicked(engRef.getAudio());
            } else if (this.playThemeButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new ThemeSelectionMenu(), engRef);
                this.playThemeButton.clicked(engRef.getAudio());
            } else if (this.paletteMenu.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new PaletteMenu(), engRef);
                this.paletteMenu.clicked(engRef.getAudio());
            }
        }
    }

    private void arrangePortrait() {
        this.mainText.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.playRandomLevelButton.setPosition(0, (int)(getH / 1.25));
        this.playRandomLevelButton.setSize(getW, getH / 8);
        this.playRandomLevelButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.playThemeButton.setPosition(0, (int) (getH / 1.75));
        this.playThemeButton.setSize(getW, getH / 8);
        this.playThemeButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.paletteMenu.setPosition(0, (int) (getH / 2.2));
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
        this.playRandomLevelButton.setPosition(0, (int)(getH / 1.25));
        this.playRandomLevelButton.setSize(getW, getH / 8);
        this.playRandomLevelButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.playThemeButton.setPosition(0, (int) (getH / 1.75));
        this.playThemeButton.setSize(getW, getH / 8);
        this.playThemeButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.paletteMenu.setPosition(0, (int) (getH / 2.2));
        this.paletteMenu.setSize(getW, getH / 8);
        this.paletteMenu.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.coinIndicator.setPosition(5 * getW / 8, 0);
        this.coinIndicator.setSize(getW / 4, getW / 8);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
    }
}
package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class ModeSelectionMenu implements IScene {
    private String mainText;
    private String mainFont;

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
        this.mainText = "Choose play mode: ";
        this.mainFont = Resources.FONT_KOMIKAX;

        // buttons
        int getW = engRef.getRender().getWidth();
        int getH = engRef.getRender().getHeight();
        this.playRandomLevelButton = new Button(0, (int) (getH / 1.25),
                getW, getH / 8, "RANDOM LEVELS", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);
        this.playThemeButton = new Button(0, (int) (getH / 1.75),
                getW, getH / 8, "THEME LEVELS", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);
        this.paletteMenu = new Button(0, (int) (getH / 2.2),
                getW, getH / 8, "PALETTES MENU", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);
        this.coinIndicator = new Button(5 * getW / 8, 0, getW / 4, getW / 8, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_EXO_REGULAR_MEDIUM, "", false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime, EngineAndroid engine) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        // text
        renderMng.setColor(0xFF000000);
        renderMng.setFont(this.mainFont);
        int textWidth = renderMng.getTextWidth(this.mainFont, this.mainText);
        renderMng.drawText((renderMng.getWidth() - textWidth) / 2, renderMng.getHeight() / 6, this.mainText);

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
}

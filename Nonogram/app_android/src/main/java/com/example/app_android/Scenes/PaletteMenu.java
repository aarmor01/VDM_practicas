package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;
import com.example.app_android.Objects.Label;
import com.example.app_android.Resources;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.Modules.RenderAndroid;

public class PaletteMenu implements IScene {
    final int PALETTE_VALUE = 3;
    private Label mainText;

    private Button[] paletteButtons;
    private Button coinIndicator;
    private Button backButton;

    @Override
    public String getId() {
        return "PaletteMenu";
    }

    @Override
    public void init(EngineAndroid engRef) {
        // main text
        this.mainText = new Label("Palette menu", 0, 0, Resources.FONT_KOMIKAX);

        // buttons
        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        int numPalettes = GameManager.getInstance().NUM_PALETTES;

        paletteButtons = new Button[numPalettes];
        for (int i = 0; i < numPalettes; i++) {
            this.paletteButtons[i] = new Button(0, 0, 0, 0,
                    "Palette " + (i + 1), "", Resources.FONT_SIMPLY_SQUARE_BIG, Resources.SOUND_BUTTON, false);
        }
        this.coinIndicator = new Button(0,0,0,0, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_EXO_REGULAR_MEDIUM, "", false);
        this.backButton = new Button(0, 0, 0, 0, "Back",
                Resources.IMAGE_BACK_BUTTON, Resources.FONT_SIMPLY_SQUARE_BIG, Resources.SOUND_BUTTON, false);

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
        mainText.render(renderMng);

        // buttons
        for(Button a : paletteButtons)
            a.render(renderMng);
        this.coinIndicator.render(renderMng);
        this.backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engRef) {
        if (input.getType() == InputType.TOUCH_UP || input.getType() == InputType.TOUCH_LONG) {
            int x = -1;
            int i = 0;
            while (x == -1 && i < GameManager.getInstance().NUM_PALETTES) {
                if (this.paletteButtons[i].isInButton(input.getX(), input.getY()))
                    x = i;
                i++;
            }

            if (x != -1) {
                GameManager gM = GameManager.getInstance();
                if (!gM.isPaletteUnlocked(x) && gM.getCoins() < PALETTE_VALUE) {
                    return;
                }
                else if(!gM.isPaletteUnlocked(x)){
                    gM.unlockPalette(x);
                    gM.addCoins(-PALETTE_VALUE);
                }
                gM.setPalette(x);
                engRef.getRender().setBackGroundColor(GameManager.getInstance().getColor(GameManager.ColorTypes.BG_COLOR.ordinal()));
                this.paletteButtons[0].clicked(engRef.getAudio());
                coinIndicator.setText(Integer.toString(gM.getCoins()));
            }

            if (this.backButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new ModeSelectionMenu(), engRef);
                this.backButton.clicked(engRef.getAudio());
            }
        }
    }

    private void arrangePortrait() {
        this.mainText.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.coinIndicator.setPosition(5 * getW / 8, 0);
        this.coinIndicator.setSize(getW / 4, getW / 8);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.backButton.setPosition(getW / 3, (getH / 6) * 5);
        this.backButton.setSize(getW / 3, (getH / 6) / 3);
        this.backButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        int numPalettes = GameManager.getInstance().NUM_PALETTES;
        for (int i = 0; i < numPalettes; i++) {
            this.paletteButtons[i].setPosition(getW / 2 - (getW / 5)*numPalettes/2 + (getW / 5 + 5)*i, (getH / 2));
            this.paletteButtons[i].setSize(getW / 5, getW / 5);
        }
    }

    private void arrangeLandscape() {
        this.mainText.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.coinIndicator.setPosition(getW - getW / 5, 0);
        this.coinIndicator.setSize(getW / 5, getW / 8);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.backButton.setPosition(getW / 3, (getH / 6) * 5);
        this.backButton.setSize(getW / 3, (getH / 6) / 3);
        this.backButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        int numPalettes = GameManager.getInstance().NUM_PALETTES;
        for (int i = 0; i < numPalettes; i++) {
            this.paletteButtons[i].setPosition(getW / 2 - (getW / 5)*numPalettes/2 + (getW / 5 + 5)*i, (getH / 2));
            this.paletteButtons[i].setSize(getW / 5, getW / 5);
        }
    }
}
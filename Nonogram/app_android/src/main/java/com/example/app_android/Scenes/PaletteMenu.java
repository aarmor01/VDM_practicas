package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;
import com.example.app_android.Resources;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.Modules.RenderAndroid;

public class PaletteMenu implements IScene {
    final int PALETTE_VALUE = 3;
    private String mainText;
    private String mainFont;

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
        this.mainText = "Palette menu";
        this.mainFont = Resources.FONT_KOMIKAX;

        // buttons
        int getW = engRef.getRender().getWidth();
        int getH = engRef.getRender().getHeight();
        int numPalettes = GameManager.getInstance().NUM_PALETTES;

        paletteButtons = new Button[numPalettes];
        for (int i = 0; i < numPalettes; i++) {
            this.paletteButtons[i] = new Button(getW / 2 - (getW / 5)*numPalettes/2 + (getW / 5 + 5)*i, (getH / 2), getW / 5, getW / 5,
                    "Palette " + (i + 1), "", Resources.FONT_SIMPLY_SQUARE_BIG, Resources.SOUND_BUTTON, false);
        }
        this.coinIndicator = new Button(5 * getW / 8, 0, getW / 4, getW / 8, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_EXO_REGULAR_MEDIUM, "", false);
        this.backButton = new Button(getW / 3, (getH / 6) * 5,
                getW / 3, (getH / 6) / 3, "Back",
                Resources.IMAGE_BACK_BUTTON, Resources.FONT_SIMPLY_SQUARE_BIG, Resources.SOUND_BUTTON, false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        // text
        renderMng.setColor(0xFF000000);
        renderMng.setFont(this.mainFont);
        int textWidth = renderMng.getTextWidth(this.mainFont, this.mainText);
        renderMng.drawText((renderMng.getWidth() - textWidth) / 2, renderMng.getHeight() / 6, this.mainText);

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
}

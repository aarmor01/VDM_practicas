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
    final int[] PALETTE_VALUE = {50, 100};
    private Label mainText;

    private Button[] paletteButtons;
    private Button coinIndicator;
    private Button backButton;

    @Override
    public String getId() {
        return "PaletteMenu";
    }

    @Override
    public void init(EngineAndroid engine) {
        // main text
        this.mainText = new Label(0, 0, "Palette menu", Resources.FONT_KOMIKAX);

        // buttons
        GameManager gM = GameManager.getInstance();
        int numPalettes = GameManager.getInstance().NUM_PALETTES;
        this.paletteButtons = new Button[numPalettes];
        for (int i = 0; i < numPalettes; i++) {
            if (gM.isPaletteUnlocked(i))
                this.paletteButtons[i] = new Button(0, 0, 0, 0,
                        "Palette " + (i + 1), "", Resources.FONT_SIMPLY_SQUARE_BIG, Resources.SOUND_BUTTON);
            else
                this.paletteButtons[i] = new Button(0, 0, 0, 0,
                       "(-" + PALETTE_VALUE[i-1] + ") Palette " + (i + 1), Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE_BIG, Resources.SOUND_BUTTON);
        }
        this.coinIndicator = new Button(0,0,0,0, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE_MEDIUM, "");
        this.backButton = new Button(0, 0, 0, 0, "Back",
                Resources.IMAGE_BACK_BUTTON, Resources.FONT_SIMPLY_SQUARE_BIG, Resources.SOUND_BUTTON);

        this.paletteButtons[gM.getActPalette()].setColor(gM.getColor(GameManager.ColorTypes.MAIN_COLOR.ordinal()));
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
        for(Button button : this.paletteButtons)
            button.render(renderer);
        this.coinIndicator.render(renderer);
        this.backButton.render(renderer);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if (input.getType() == InputType.TOUCH_UP || input.getType() == InputType.TOUCH_LONG) {
            int palette_id = 0;
            while (palette_id < GameManager.getInstance().NUM_PALETTES) {
                if (this.paletteButtons[palette_id].isInButton(input.getX(), input.getY()))
                    break;
                palette_id++;
            }

            if (palette_id < GameManager.getInstance().NUM_PALETTES) {
                GameManager gM = GameManager.getInstance();
                if (!gM.isPaletteUnlocked(palette_id) && gM.getCoins() < this.PALETTE_VALUE[palette_id-1])
                    return;
                else if(!gM.isPaletteUnlocked(palette_id)){
                    gM.unlockPalette(palette_id);
                    gM.addCoins(-this.PALETTE_VALUE[palette_id-1]);
                }

                gM.setPalette(palette_id);
                engine.getRender().setBackGroundColor(GameManager.getInstance().getColor(GameManager.ColorTypes.BG_COLOR.ordinal()));
                updatePalette();

                this.paletteButtons[0].clicked(engine.getAudio());
                this.coinIndicator.setText(Integer.toString(gM.getCoins()));
            }

            if (this.backButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new ModeSelectionMenu(), engine);
                this.backButton.clicked(engine.getAudio());
            }
        }
    }

    private void arrangePortrait() {
        this.mainText.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.coinIndicator.setPosition(5 * getW / 8, 0);
        this.coinIndicator.setSize(getW / 4, getW / 8);

        this.backButton.setPosition(getW / 3, (getH / 6) * 5);
        this.backButton.setSize(getW / 3, (getH / 6) / 3);

        int numPalettes = GameManager.getInstance().NUM_PALETTES;
        for (int i = 0; i < numPalettes; i++) {
            this.paletteButtons[i].setPosition(getW/4, (getH / 4) + (getH / 5 + 5)*i);
            this.paletteButtons[i].setSize((int)(getW/1.80f), getW / 5);
        }
    }

    private void arrangeLandscape() {
        this.mainText.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        int getW = GameManager.getInstance().getWidth();
        int getH = GameManager.getInstance().getHeight();
        this.coinIndicator.setPosition(getW - getW / 5, 0);
        this.coinIndicator.setSize(getW / 5, getW / 8);

        this.backButton.setPosition(getW / 3, (getH / 6) * 5);
        this.backButton.setSize(getW / 3, (getH / 6) / 3);

        int numPalettes = GameManager.getInstance().NUM_PALETTES;
        for (int i = 0; i < numPalettes; i++) {
            this.paletteButtons[i].setPosition(getW / 3, (getH / 4) + (getW / 10 + 5)*i);
            this.paletteButtons[i].setSize(getW/3, getH / 10);
        }
    }

    private void updatePalette(){
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
        this.backButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        int numPalettes = GameManager.getInstance().NUM_PALETTES;
        for (int palette = 0; palette < numPalettes; palette++) {
            if(GameManager.getInstance().isPaletteUnlocked(palette)){
                this.paletteButtons[palette].setImage("");
                this.paletteButtons[palette].setText("Palette " + (palette + 1));
            }

            this.paletteButtons[palette].setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
        }
        this.paletteButtons[GameManager.getInstance().getActPalette()].setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.MAIN_COLOR.ordinal()));
    }
}

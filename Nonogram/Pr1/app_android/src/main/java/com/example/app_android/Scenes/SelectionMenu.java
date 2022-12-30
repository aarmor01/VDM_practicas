package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class SelectionMenu implements IScene {
    private Button backButton;

    private Button t4x4;
    private Button t5x5;
    private Button t5x10;
    private Button t8x8;
    private Button t10x10;
    private Button t10x15;

    @Override
    public String getId() {
        return "SelectionMenu";
    }

    @Override
    public void init(EngineAndroid engRef) {
        // select buttons
        this.t4x4 = new Button(0, 0, 0, 0, "4x4", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.t5x5 = new Button(0, 0, 0, 0, "5x5", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.t5x10 = new Button(0, 0, 0, 0, "10x5", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.t8x8 = new Button(0, 0, 0, 0, "8x8", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.t10x10 = new Button(0, 0, 0, 0, "10x10", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.t10x15 = new Button(0, 0, 0, 0, "15x10", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);

        // back button
        this.backButton = new Button(0, 0, 0, 0, "Back", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);

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
        renderMng.setColor(0xFF000000);

        this.t4x4.render(renderMng);
        this.t5x5.render(renderMng);
        this.t5x10.render(renderMng);
        this.t8x8.render(renderMng);
        this.t10x10.render(renderMng);
        this.t10x15.render(renderMng);

        this.backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engRef) {
        if (input.getType() == InputType.TOUCH_UP) {
            int x = 0, y = 0;
            if (this.t4x4.isInButton(input.getX(), input.getY())) {
                x = 4;
                y = 4;
            } else if (this.t5x5.isInButton(input.getX(), input.getY())) {
                x = 5;
                y = 5;
            } else if (this.t5x10.isInButton(input.getX(), input.getY())) {
                x = 10;
                y = 5;
            } else if (this.t8x8.isInButton(input.getX(), input.getY())) {
                x = 8;
                y = 8;
            } else if (this.t10x10.isInButton(input.getX(), input.getY())) {
                x = 10;
                y = 10;
            } else if (this.t10x15.isInButton(input.getX(), input.getY())) {
                x = 15;
                y = 10;
            } else if (this.backButton.isInButton(input.getX(), input.getY())) {
                engRef.getSceneManager().changeScene(new ModeSelectionMenu(), engRef);
                this.backButton.clicked(engRef.getAudio());
            }

            if (x != 0) {
                this.backButton.clicked(engRef.getAudio());
                engRef.getSceneManager().changeScene(new BoardScene(x, y), engRef);
            }
        }
    }


    private void arrangePortrait() {
        // select buttons
        int x = GameManager.getInstance().getWidth() / 3;
        int y = GameManager.getInstance().getHeight() / 6;
        this.t4x4.setPosition(x / 4, y * 2);
        this.t4x4.setSize(x / 2, x / 2);
        this.t5x5.setPosition(x * 5 / 4, y * 2);
        this.t5x5.setSize(x / 2, x / 2);
        this.t5x10.setPosition(x * 9 / 4, y * 2);
        this.t5x10.setSize(x / 2, x / 2);
        this.t8x8.setPosition(x / 4, y * 3);
        this.t8x8.setSize(x / 2, x / 2);
        this.t10x10.setPosition(x * 5 / 4, y * 3);
        this.t10x10.setSize(x / 2, x / 2);
        this.t10x15.setPosition(x * 9 / 4, y * 3);
        this.t10x15.setSize(x / 2, x / 2);

        // back buttons
        this.backButton.setPosition(x, y * 5);
        this.backButton.setSize(x, y / 3);
    }

    private void arrangeLandscape() {
        // select buttons
        int x = GameManager.getInstance().getWidth() / 3;
        int y = GameManager.getInstance().getHeight() / 6;
        this.t4x4.setPosition(x / 4, y);
        this.t4x4.setSize(x / 2, x / 2);
        this.t5x5.setPosition(x * 5 / 4, y);
        this.t5x5.setSize(x / 2, x / 2);
        this.t5x10.setPosition(x * 9 / 4, y);
        this.t5x10.setSize(x / 2, x / 2);
        this.t8x8.setPosition(x / 4, y * 3);
        this.t8x8.setSize(x / 2, x / 2);
        this.t10x10.setPosition(x * 5 / 4, y * 3);
        this.t10x10.setSize(x / 2, x / 2);
        this.t10x15.setPosition(x * 9 / 4, y * 3);
        this.t10x15.setSize(x / 2, x / 2);

        // back buttons
        this.backButton.setPosition(x, y * 5);
        this.backButton.setSize(x, y / 3);
    }
}
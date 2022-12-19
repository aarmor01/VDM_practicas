package com.example.app_android.Scenes;

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
        int x = engRef.getRender().getWidth() / 3;
        int y = engRef.getRender().getHeight() / 6;
        this.t4x4 = new Button(x / 4, y * 2, x / 2, x / 2, "4x4", "",
                Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
        this.t5x5 = new Button(x * 5 / 4, y * 2, x / 2, x / 2, "5x5", "",
                Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
        this.t5x10 = new Button(x * 9 / 4, y * 2, x / 2, x / 2, "10x5", "",
                Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
        this.t8x8 = new Button(x / 4, y * 3, x / 2, x / 2, "8x8", "",
                Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
        this.t10x10 = new Button(x * 5 / 4, y * 3, x / 2, x / 2, "10x10", "",
                Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
        this.t10x15 = new Button(x * 9 / 4, y * 3, x / 2, x / 2, "15x10", "",
                Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);

        this.backButton = new Button(x, y * 5, x, y / 3, "Back", "",
                Resources.FONT_SIMPLY_SQUARE, Resources.SOUND_BUTTON, false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime) {

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
}
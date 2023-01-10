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
    public void init(EngineAndroid engine) {
        // select buttons
        this.t4x4 = new Button(0, 0, 0, 0, "4x4", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t5x5 = new Button(0, 0, 0, 0, "5x5", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t5x10 = new Button(0, 0, 0, 0, "5x10", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t8x8 = new Button(0, 0, 0, 0, "8x8", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t10x10 = new Button(0, 0, 0, 0, "10x10", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t10x15 = new Button(0, 0, 0, 0, "10x15", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);

        // back button
        this.backButton = new Button(0, 0, 0, 0, "Back", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);

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
        renderer.setColor(0xFF000000);

        this.t4x4.render(renderer);
        this.t5x5.render(renderer);
        this.t5x10.render(renderer);
        this.t8x8.render(renderer);
        this.t10x10.render(renderer);
        this.t10x15.render(renderer);

        this.backButton.render(renderer);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if (input.getType() == InputType.TOUCH_UP) {
            int numRows = 0, numCols = 0;
            if (this.t4x4.isInButton(input.getX(), input.getY())) {
                numRows = 4;
                numCols = 4;
            } else if (this.t5x5.isInButton(input.getX(), input.getY())) {
                numRows = 5;
                numCols = 5;
            } else if (this.t5x10.isInButton(input.getX(), input.getY())) {
                numRows = 5;
                numCols = 10;
            } else if (this.t8x8.isInButton(input.getX(), input.getY())) {
                numRows = 8;
                numCols = 8;
            } else if (this.t10x10.isInButton(input.getX(), input.getY())) {
                numRows = 10;
                numCols = 10;
            } else if (this.t10x15.isInButton(input.getX(), input.getY())) {
                numRows = 10;
                numCols = 15;
            } else if (this.backButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().changeScene(new ModeSelectionMenu(), engine);
                this.backButton.clicked(engine.getAudio());
            }

            if (numRows != 0 && numCols != 0) {
                this.t4x4.clicked(engine.getAudio());
                engine.getSceneManager().changeScene(new BoardScene(numRows, numCols), engine);
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

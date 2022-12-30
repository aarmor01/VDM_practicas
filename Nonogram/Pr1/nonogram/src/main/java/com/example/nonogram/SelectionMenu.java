package com.example.nonogram;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.InputType;

public class SelectionMenu implements IScene {
    private Button t4x4;
    private Button t5x5;
    private Button t10x5;
    private Button t8x8;
    private Button t10x10;
    private Button t15x10;
    private Button backButton;

    @Override
    public void init() {
        int gameWidth = GameManager.getInstance().getWidth() / 3;
        int gameHeight = GameManager.getInstance().getHeight() / 6;

        this.t4x4 = new Button(gameWidth / 4, gameHeight * 2, gameWidth / 2, gameWidth / 2, "4x4", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t5x5 = new Button(gameWidth * 5 / 4, gameHeight * 2, gameWidth / 2, gameWidth / 2, "5x5", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t10x5 = new Button(gameWidth * 9 / 4, gameHeight * 2, gameWidth / 2, gameWidth / 2, "10x5", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t8x8 = new Button(gameWidth / 4, gameHeight * 3, gameWidth / 2, gameWidth / 2, "8x8", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t10x10 = new Button(gameWidth * 5 / 4, gameHeight * 3, gameWidth / 2, gameWidth / 2, "10x10", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.t15x10 = new Button(gameWidth * 9 / 4, gameHeight * 3, gameWidth / 2, gameWidth / 2, "15x10", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.backButton = new Button(gameWidth, gameHeight * 5, gameWidth, gameHeight / 3, "Back", "",
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
    }

    @Override
    public void update(double deltaTime, IEngine engine) {}

    @Override
    public void render(IRender renderer) {
        this.t4x4.render(renderer);
        this.t5x5.render(renderer);
        this.t10x5.render(renderer);
        this.t8x8.render(renderer);
        this.t10x10.render(renderer);
        this.t15x10.render(renderer);
        this.backButton.render(renderer);
    }

    @Override
    public void handleInput(IInput input, IEngine engine) {
        if (input.getType() == InputType.TOUCH_UP) {
            int numRows = 0, numCols = 0;
            if (this.t4x4.isInButton(input.getX(), input.getY())) {
                numRows = 4;
                numCols = 4;
            } else if (this.t5x5.isInButton(input.getX(), input.getY())) {
                numRows = 5;
                numCols = 5;
            } else if (this.t10x5.isInButton(input.getX(), input.getY())) {
                numRows = 10;
                numCols = 5;
            } else if (this.t8x8.isInButton(input.getX(), input.getY())) {
                numRows = 8;
                numCols = 8;
            } else if (this.t10x10.isInButton(input.getX(), input.getY())) {
                numRows = 10;
                numCols = 10;
            } else if (this.t15x10.isInButton(input.getX(), input.getY())) {
                numRows = 15;
                numCols = 10;
            } else if (this.backButton.isInButton(input.getX(), input.getY())) {
                engine.getSceneManager().popScene();
                this.backButton.clicked(engine.getAudio());
            }

            if (numRows != 0 && numCols != 0) {
                this.t4x4.clicked(engine.getAudio());
                engine.getSceneManager().pushScene(new BoardScene(numCols, numRows));
            }
        }
    }
}

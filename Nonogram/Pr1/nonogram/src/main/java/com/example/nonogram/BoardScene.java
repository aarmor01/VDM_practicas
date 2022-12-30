package com.example.nonogram;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.shared.InputType;

//Clase interna que representa la escena que queremos pintar
public class BoardScene implements IScene {
    private int dim_w, dim_h;

    private Board board;
    private Button checkButton;
    private Button backButton;

    public BoardScene(int w, int h) {
        this.dim_w = w;
        this.dim_h = h;
    }

    @Override
    public void init() {
        int gameWidth = GameManager.getInstance().getWidth();
        int gameHeight = GameManager.getInstance().getHeight();

        // buttons
        this.checkButton = new Button((gameWidth - (gameWidth / 3)) * 4 / 5, gameHeight / 9,
                gameWidth / 3, gameHeight / 12, "Check", Resources.IMAGE_CHECK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.backButton = new Button((gameWidth - (gameWidth / 3)) / 5, gameHeight / 9,
                gameWidth / 3, gameHeight / 12, "Back", Resources.IMAGE_BACK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);

        // init board
        this.board = new Board();
        this.board.init(this.dim_w, this.dim_h);
    }

    @Override
    public void update(double deltaTime, IEngine engine) {
        this.board.update(deltaTime);
    }

    @Override
    public void render(IRender renderer) {
        // buttons
        this.checkButton.render(renderer);
        this.backButton.render(renderer);

        // board
        this.board.render(renderer);
    }

    @Override
    public void handleInput(IInput input, IEngine engine) {
        if (input.getType() == InputType.TOUCH_UP) {
            if (this.board.isInBoard(input.getX(), input.getY())) {
                engine.getAudio().playSound(Resources.SOUND_CLICK);
                this.board.markCell(input.getX(), input.getY());
            } else if (this.checkButton.isInButton(input.getX(), input.getY())) {
                this.board.check();
                this.checkButton.clicked(engine.getAudio());
                if (this.board.win)
                    engine.getSceneManager().pushScene(new WinScene(this.board));
            } else if (this.backButton.isInButton(input.getX(), input.getY())) {
                this.backButton.clicked(engine.getAudio());
                engine.getSceneManager().popScene();
            }
        }
    }
}
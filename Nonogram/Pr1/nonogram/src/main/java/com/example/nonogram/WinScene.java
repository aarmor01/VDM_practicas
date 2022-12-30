package com.example.nonogram;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.InputType;

public class WinScene implements IScene {
    private Label winText;
    private Board board;
    private Button backButton;

    public WinScene(Board board) {
        this.board = board;
    }

    @Override
    public void init() {
        int gameWidth = GameManager.getInstance().getWidth();
        int gameHeight = GameManager.getInstance().getHeight();

        this.winText = new Label(gameWidth / 2, gameHeight / 6, "¡¡Victoria!!", Resources.FONT_EXO_REGULAR_BIG);

        this.backButton = new Button((gameWidth - gameWidth / 4) / 2, gameHeight * 7 / 8,
                gameWidth / 4, gameHeight / 12, "Back", Resources.IMAGE_BACK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
    }

    @Override
    public void update(double deltaTime, IEngine engine) {}

    @Override
    public void render(IRender renderer) {
        // text
        this.winText.render(renderer);

        // completed board
        this.board.renderWin(renderer);

        // back button
        this.backButton.render(renderer);
    }

    @Override
    public void handleInput(IInput input, IEngine engine) {
        if (input.getType() == InputType.TOUCH_UP && this.backButton.isInButton(input.getX(), input.getY())) {
            while (engine.getSceneManager().getStackSize() > 1)
                engine.getSceneManager().popScene();
            this.backButton.clicked(engine.getAudio());
        }
    }
}

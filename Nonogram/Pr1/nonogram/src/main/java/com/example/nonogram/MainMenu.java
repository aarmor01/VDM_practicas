package com.example.nonogram;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.InputType;

public class MainMenu implements IScene {
    private Label title;
    private Button playButton;

    @Override
    public void init() {
        int gameWidth = GameManager.getInstance().getWidth();
        int gameHeight = GameManager.getInstance().getHeight();

        this.title = new Label(gameWidth / 2, gameHeight / 6, "NONOGRAMAS", Resources.FONT_KOMIKAX);

        this.playButton = new Button((gameWidth - gameWidth / 3) / 2, (int) (gameHeight / 1.5),
                gameWidth / 3, gameHeight / 8, "PLAY", "",
                Resources.FONT_EXO_REGULAR_BIG, Resources.SOUND_BUTTON);
    }

    @Override
    public void update(double deltaTime, IEngine engine) {}

    @Override
    public void render(IRender renderer) {
        // text
        this.title.render(renderer);

        // button
        this.playButton.render(renderer);
    }

    @Override
    public void handleInput(IInput input, IEngine engine) {
        if (input.getType() == InputType.TOUCH_UP && this.playButton.isInButton(input.getX(), input.getY())) {
            engine.getSceneManager().pushScene(new SelectionMenu());
            this.playButton.clicked(engine.getAudio());
        }
    }
}

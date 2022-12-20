package com.example.app_android.Scenes;

import com.example.app_android.Objects.Label;
import com.example.app_android.Resources;
import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class MainMenu implements IScene {
    private Label title;
    private Button playButton;

    @Override
    public String getId() {
        return "MainMenu";
    }

    @Override
    public void init(EngineAndroid engRef) {
        this.title = new Label("NONOGRAMAS", 0, 0, Resources.FONT_KOMIKAX);
        this.playButton = new Button(0, 0, 0, 0, "PLAY", "", Resources.FONT_EXO_REGULAR_MEDIUM,
                Resources.SOUND_BUTTON, false);

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
        // render title
        this.title.render(renderMng);
        // render button
        this.playButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if (input.getType() == InputType.TOUCH_UP && this.playButton.isInButton(input.getX(), input.getY())) {
            engine.getSceneManager().changeScene(new ModeSelectionMenu(), engine);
            this.playButton.clicked(engine.getAudio());
//            engine.getIntentSystemAndroid().share(TWITTER, "hehe twitter que guapo");
        }
    }

    private void arrangePortrait() {
        this.title.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        this.playButton.setPosition((2 * GameManager.getInstance().getWidth() / 3) / 2,
                (int)(GameManager.getInstance().getHeight() / 1.5));
        this.playButton.setSize(GameManager.getInstance().getWidth() / 3, GameManager.getInstance().getHeight() / 8);
        this.playButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
    }

    private void arrangeLandscape() {
        this.title.setPos((GameManager.getInstance().getWidth()) / 2, GameManager.getInstance().getHeight() / 6);

        this.playButton.setPosition((2 * GameManager.getInstance().getWidth() / 3) / 2,
                (int)(GameManager.getInstance().getHeight() / 1.5));
        this.playButton.setSize(GameManager.getInstance().getWidth() / 3, GameManager.getInstance().getHeight() / 8);
        this.playButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
    }
}
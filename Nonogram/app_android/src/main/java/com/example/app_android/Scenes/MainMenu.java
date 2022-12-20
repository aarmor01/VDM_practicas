package com.example.app_android.Scenes;

import com.example.app_android.Resources;
import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class MainMenu implements IScene {
    private String title;
    private String fontTitle;

    private Button playButton;

    @Override
    public String getId() {
        return "MainMenu";
    }

    @Override
    public void init(EngineAndroid engRef) {
        if (engRef.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait(engRef);
        else if (engRef.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape(engRef);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {
        if (engRef.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait(engRef);
        else if (engRef.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape(engRef);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        // render title
        renderMng.setColor(0xFF000000);
        renderMng.setFont(this.fontTitle);
        int textWidth = renderMng.getTextWidth(this.fontTitle, this.title);
        renderMng.drawText((GameManager.getInstance().getWidth() - textWidth)/2, GameManager.getInstance().getHeight()/6, title);
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

    private void arrangePortrait(EngineAndroid engRef) {
        this.title = "NONOGRAMAS";
        this.fontTitle = Resources.FONT_KOMIKAX;
        this.playButton = new Button((GameManager.getInstance().getWidth() - GameManager.getInstance().getWidth() / 3) / 2,
                (int) (GameManager.getInstance().getHeight() / 1.5), GameManager.getInstance().getWidth() / 3,
                GameManager.getInstance().getHeight() / 8, "PLAY", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);

//        engRef.getAudio().loadMusic("./assets/sounds/puzzleTheme.wav", 0.1f);
//        engRef.getAudio().playMusic();
    }

    private void arrangeLandscape(EngineAndroid engRef) {
        this.title = "NANOGRAMA";
        this.fontTitle = Resources.FONT_KOMIKAX;
        this.playButton = new Button((GameManager.getInstance().getWidth() - GameManager.getInstance().getWidth() / 3) / 2,
                (int) (GameManager.getInstance().getHeight() / 1.5), GameManager.getInstance().getWidth() / 3,
                GameManager.getInstance().getHeight() / 8, "PLAY", "", Resources.FONT_EXO_REGULAR_MEDIUM, Resources.SOUND_BUTTON, false);
    }
}
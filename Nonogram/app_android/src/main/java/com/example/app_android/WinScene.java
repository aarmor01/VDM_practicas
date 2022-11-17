package com.example.app_android;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.FontType;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.InputType;
import com.example.engine_android.RenderAndroid;

public class WinScene implements IScene {
    private Board board;
    private String winText;
    private String font;
    private Button backButton;

    private EngineAndroid engRef;

    public WinScene(Board b){
        board = b;
    }

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        winText = "¡¡Victoria!!";
        font = engRef.getRender().loadFont("./assets/fonts/Exo-Regular.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 8);
        String fontButtons = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        backButton = new Button((engRef.getRender().getWidth()-engRef.getRender().getWidth()/4)/2, engRef.getRender().getHeight()*7/8,
                engRef.getRender().getWidth()/4, engRef.getRender().getHeight()/12, "Back", engRef.getRender().loadImage("./assets/images/backbutton.png"), fontButtons, btAudio);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int w = renderMng.getTextWidth(font, winText);
        renderMng.drawText((renderMng.getWidth()-w)/2, renderMng.getHeight()/6, winText);
        board.renderWin(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        if(input.getType() == InputType.TOUCH_UP && backButton.isInButton(input.getX(), input.getY())){
            while(engRef.getSceneManager().getStackSize() > 1)
                engRef.getSceneManager().popScene();
            backButton.clicked(engRef.getAudio());
        }
    }
}

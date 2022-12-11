package com.example.app_android.Scenes;

import com.example.app_android.Objects.Board;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class WinScene implements IScene {
    private final String MENU = "LevelHistorySelectionMenu";
    private Board board;
    private String winText;
    private String font;
    private Button backButton;
    private boolean victory;

    private EngineAndroid engRef;

    public WinScene(Board b, boolean win){
        board = b;
        victory = win;
    }

    @Override
    public String getId(){return "WinScene";}

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        if(victory)
            winText = "¡¡Victoria!!";
        else
            winText = "Derrota :(";
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
        if(victory)
            board.renderWin(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        if(input.getType() == InputType.TOUCH_UP && backButton.isInButton(input.getX(), input.getY())){
            while(engRef.getSceneManager().currentScene().getId() != MENU)
                engRef.getSceneManager().popScene();
            backButton.clicked(engRef.getAudio());
        }
    }
}

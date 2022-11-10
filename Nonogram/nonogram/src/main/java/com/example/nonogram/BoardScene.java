package com.example.nonogram;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.shared.FontType;
import com.example.engine_common.shared.InputType;

//Clase interna que representa la escena que queremos pintar
public class BoardScene implements IScene {
    private int dim_w, dim_h;

    private Board board;
    private Button checkButton;
    private Button backButton;

    private IEngine engRef;
    private String sound;

    public BoardScene(int w, int h) {
        this.dim_w = w; this.dim_h = h;
    }

    @Override
    public void init(IEngine engine) {
        board = new Board();
        board.init(dim_w, dim_h, engine);

        engRef = engine;

        String fontButtons = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        checkButton = new Button((engRef.getRender().getWidth() - (engRef.getRender().getWidth()/3))/5, engRef.getRender().getHeight()/9,
                engRef.getRender().getWidth()/3, engRef.getRender().getHeight()/12, "Check" ,engRef.getRender().loadImage("./assets/images/checkbutton.png"), fontButtons, btAudio);
        backButton = new Button((engRef.getRender().getWidth()- (engRef.getRender().getWidth()/3))*4/5, engRef.getRender().getHeight()/9,
                engRef.getRender().getWidth()/3, engRef.getRender().getHeight()/12, "Back", engRef.getRender().loadImage("./assets/images/backbutton.png"), fontButtons, btAudio);

        sound = engine.getAudio().loadSound("./assets/sounds/click.wav", 1);
    }

    @Override
    public void update(double deltaTime) {board.update(deltaTime);}

    @Override
    public void render(IRender renderMng) {
        board.render(renderMng);
        checkButton.render(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(IInput input) {
        if(input.getType() == InputType.TOUCH_UP){
            if(board.isInBoard(input.getX(), input.getY())){
                engRef.getAudio().playSound(sound);
                board.markCell(input.getX(),input.getY());
            }
            else if(checkButton.isInButton(input.getX(), input.getY())){
                board.checkear();
                checkButton.clicked(engRef.getAudio());
                if(board.win)
                    engRef.getSceneManager().pushScene(new WinScene(board));
            }
            else if(backButton.isInButton(input.getX(), input.getY())){
                backButton.clicked(engRef.getAudio());
                engRef.getSceneManager().popScene();
            }
        }
    }
}
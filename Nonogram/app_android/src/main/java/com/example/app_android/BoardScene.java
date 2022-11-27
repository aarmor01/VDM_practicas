package com.example.app_android;

import android.os.Debug;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.FontType;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.InputType;
import com.example.engine_android.RenderAndroid;

//Clase interna que representa la escena que queremos pintar
public class BoardScene implements IScene {
    static int MAX_LIVES = 3;
    private int dim_w, dim_h;

    private Board board;
    private int lives;

    private Button checkButton;
    private Button backButton;

    private EngineAndroid engRef;
    private String sound, liveImage, noLiveImage;

    public BoardScene(int w, int h) {
        this.dim_w = w;
        this.dim_h = h;
    }

    @Override
    public void init(EngineAndroid engine) {
        lives = MAX_LIVES;
        board = new Board();
        board.init(dim_w, dim_h, engine);

        engRef = engine;

        String fontButtons = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        checkButton = new Button((engRef.getRender().getWidth() - (engRef.getRender().getWidth() / 3)) / 5, engRef.getRender().getHeight() / 9,
                engRef.getRender().getWidth() / 3, engRef.getRender().getHeight() / 12, "Check", engRef.getRender().loadImage("./assets/images/checkbutton.png"), fontButtons, btAudio);
        backButton = new Button((engRef.getRender().getWidth() - (engRef.getRender().getWidth() / 3)) * 4 / 5, engRef.getRender().getHeight() / 9,
                engRef.getRender().getWidth() / 3, engRef.getRender().getHeight() / 12, "Back", engRef.getRender().loadImage("./assets/images/backbutton.png"), fontButtons, btAudio);

        sound = engine.getAudio().loadSound("./assets/sounds/click.wav", 1);
        liveImage = engine.getRender().loadImage("./assets/images/heart.png");
        noLiveImage = engine.getRender().loadImage("./assets/images/no_heart.png");
    }

    @Override
    public void update(double deltaTime) {
        board.update(deltaTime);

    }

    @Override
    public void render(RenderAndroid renderMng) {
        board.render(renderMng);
        //checkButton.render(renderMng);
        backButton.render(renderMng);
        for (int i = MAX_LIVES; i > 0; i--) {
            String imName;
            if(i>lives)
                imName = noLiveImage;
            else
                imName = liveImage;
            renderMng.drawImage((engRef.getRender().getWidth() - (engRef.getRender().getWidth() / 3)) / 5 + (engRef.getRender().getWidth() / 15 * (MAX_LIVES - i)),
                    engRef.getRender().getHeight() / 9, engRef.getRender().getWidth() / 15, engRef.getRender().getWidth() / 15, imName);
        }
    }

    @Override
    public void handleInput(InputAndroid input) {
        if (input.getType() == InputType.TOUCH_UP) {
            if (board.isInBoard(input.getX(), input.getY())) {
                engRef.getAudio().playSound(sound);
                lives -= board.markCell(input.getX(), input.getY(), false);
                board.checkear(input.getX(), input.getY());
                if (board.win)
                    engRef.getSceneManager().pushScene(new WinScene(board, true));
                if(lives == 0)
                    engRef.getSceneManager().pushScene(new WinScene(board, false));
            }
            else if (backButton.isInButton(input.getX(), input.getY())) {
                backButton.clicked(engRef.getAudio());
                engRef.getSceneManager().popScene();
            }
        }
        else if (input.getType() == InputType.TOUCH_LONG) {
            if (board.isInBoard(input.getX(), input.getY())) {
                board.markCell(input.getX(), input.getY(), true);
            }
        }
    }
}
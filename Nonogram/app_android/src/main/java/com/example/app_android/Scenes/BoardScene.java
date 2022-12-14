package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Board;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

import java.util.ArrayList;

//Clase interna que representa la escena que queremos pintar
public class BoardScene implements IScene {
    static int MAX_LIVES = 3;
    private int dim_w, dim_h;
    String route, level;

    private Board board;
    private int lives, actLevel, actCategory;

    private Button backButton;
    private Button coinIndicator;
    private Button recoverLive;

    private EngineAndroid engRef;
    private String sound, liveImage, noLiveImage;

    public BoardScene(int w, int h) {
        this.dim_w = w;
        this.dim_h = h;
    }

    public BoardScene(String r, int file, int category){
        route = r;
        level = file + ".txt";
        actLevel = file;
        actCategory = category;
    }

    @Override
    public String getId(){return "BoardScene";}

    @Override
    public void init(EngineAndroid engine) {
        lives = MAX_LIVES;
        engRef = engine;
        if(GameManager.getInstance().getSavedBoard(actCategory) != null){
            board = GameManager.getInstance().getSavedBoard(actCategory);
            GameManager.getInstance().resetBoard(actCategory);
        }
        else{
            board = new Board();

            if(dim_h != 0)
                board.init(dim_w, dim_h, engRef, null);
            else{
                ArrayList<String> bf = engRef.readText(route, level);
                board.initFile(bf, engRef);
            }
        }

        String fontButtons = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT,
                GameManager.getInstance().getWidth() / 22);
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);

        int w = GameManager.getInstance().getWidth() / 3;
        int h = GameManager.getInstance().getHeight() / 12;
        backButton = new Button(2 * w / 5, h / 2, w, h, "Back", engRef.getRender().loadImage("./assets/images/backbutton.png"),
                fontButtons, btAudio, false);
        recoverLive = new Button(8 * w / 5, h * 2, w, h, "Recover\n live", engRef.getRender().loadImage("./assets/images/backbutton.png"),
                fontButtons, btAudio, false);
        coinIndicator = new Button(8 * w / 5, h / 2, w, h, Integer.toString(GameManager.getInstance().getCoins()),
                engRef.getRender().loadImage("./assets/images/coin.png"), fontButtons, "", false);

        sound = engine.getAudio().loadSound("./assets/sounds/click.wav", 1);
        liveImage = engine.getRender().loadImage("./assets/images/heart.png");
        noLiveImage = engine.getRender().loadImage("./assets/images/no_heart.png");
    }

    @Override
    public void update(double deltaTime) {
        if(this.engRef.getAdSystem().hasRewardBeenGranted())
            GameManager.getInstance().addCoins(69);

        board.update(deltaTime); }

    @Override
    public void render(RenderAndroid renderMng) {
        board.render(renderMng);
        backButton.render(renderMng);
        recoverLive.render(renderMng);
        coinIndicator.render(renderMng);

        int getW = GameManager.getInstance().getWidth();
        int w = getW / 9;
        for (int i = MAX_LIVES; i > 0; i--) {
            String imName;
            if(i>lives)
                imName = noLiveImage;
            else
                imName = liveImage;
            renderMng.drawImage(2 * getW / 15 + (w * (MAX_LIVES - i)), GameManager.getInstance().getHeight() / 6, w, w, imName);
        }
    }

    @Override
    public void handleInput(InputAndroid input) {
        if (input.getType() == InputType.TOUCH_UP) {
            if (board.isInBoard(input.getX(), input.getY())) {          //Input en la zona del tablero
                engRef.getAudio().playSound(sound);
                lives -= board.markCell(input.getX(), input.getY(), false);

                if (board.checkear(input.getX(), input.getY())){            //Checkeo de la victoria
                    GameManager.getInstance().updateCategory(actCategory, actLevel, null);
                    engRef.getSceneManager().pushScene(new WinScene(board, true));
                }
                if(lives == 0)
                    engRef.getSceneManager().pushScene(new WinScene(board, false));
            }
            else if (backButton.isInButton(input.getX(), input.getY())) {   //Input boton de volver
                backButton.clicked(engRef.getAudio());
                engRef.getSceneManager().popScene();

                GameManager.getInstance().updateCategory(actCategory, actLevel - 1, board);
            }
            else if (coinIndicator.isInButton(input.getX(), input.getY())) {   //Input boton anuncio
                this.engRef.getAdSystem().showRewardedAd();
            }
        }
        else if (input.getType() == InputType.TOUCH_LONG) {     //Long touch en tablero
            if (board.isInBoard(input.getX(), input.getY())) {
                board.markCell(input.getX(), input.getY(), true);
            }
        }
    }
}
package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.app_android.Objects.Board;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

import java.util.ArrayList;

//Clase interna que representa la escena que queremos pintar
public class BoardScene implements IScene {
    private final int MAX_LIVES = 3;

    private int dim_w, dim_h;
    private String path, level;

    private Board board;
    private int lives, actLevel, actCategory;

    private Button backButton;
    private Button coinIndicator;
    private Button recoverLive;

    private String sound, liveImage, noLiveImage;

    public BoardScene(int w, int h) {
        this.dim_w = w;
        this.dim_h = h;
    }

    public BoardScene(String path, int file, int category) {
        this.path = path;
        this.level = file + ".txt";
        this.actLevel = file;
        this.actCategory = category;
    }

    @Override
    public String getId() {
        return "BoardScene";
    }

    @Override
    public void init(EngineAndroid engRef) {
        // scene set up
        this.lives = MAX_LIVES;
        // board creation
        this.board = new Board();
        if (this.dim_h != 0)
            this.board.init(this.dim_w, this.dim_h, engRef, null);
        else {
            ArrayList<String> bf = engRef.readText(this.path, this.level);
            this.board.initFile(bf, engRef);
        }
        // creation needed to not save all data on file (we don't need if cell is the
        // answer, just if it was marked or not, and how)
        if (GameManager.getInstance().getSavedBoardLevel(this.actCategory) == this.actLevel - 1 &&
            GameManager.getInstance().getSavedBoardState(this.actCategory) != null) {
            this.lives = GameManager.getInstance().getSavedBoardLives(this.actCategory);
            this.board.setBoardState(GameManager.getInstance().getSavedBoardState(this.actCategory));
        }
        // we reset independently of board selected, if a user goes to another board
        // it loses the save state
        GameManager.getInstance().resetBoard(this.actCategory);

        // board ui
        this.sound = Resources.SOUND_CLICK;
        this.liveImage = Resources.IMAGE_HEART;
        this.noLiveImage = Resources.IMAGE_NO_HEART;

        // buttons
        int w = engRef.getRender().getWidth() / 3;
        int h = engRef.getRender().getHeight() / 12;
        this.backButton = new Button(2 * w / 5, h / 2, w, h, "Back", Resources.IMAGE_BACK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.recoverLive = new Button(8 * w / 5, h * 2, w, h, "Recover\n live", Resources.IMAGE_BACK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.coinIndicator = new Button(8 * w / 5, h / 2, w, h, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE_MEDIUM, "", false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime) {
//        if(engine.getAdSystem().hasRewardBeenGranted())
//            GameManager.getInstance().addCoins(69);

        this.board.update(deltaTime);
    }

    @Override
    public void render(RenderAndroid renderMng) {
        // board
        this.board.render(renderMng);

        // buttons
        this.backButton.render(renderMng);
        this.recoverLive.render(renderMng);
        this.coinIndicator.render(renderMng);

        // pendingBoardLives
        int getW = renderMng.getWidth();
        int w = getW / 9;
        for (int i = MAX_LIVES; i > 0; i--) {
            String imName;
            if (i > this.lives)
                imName = this.noLiveImage;
            else
                imName = this.liveImage;
            renderMng.drawImage(2 * getW / 15 + (w * (this.MAX_LIVES - i)), renderMng.getHeight() / 6, w, w, imName);
        }
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if (input.getType() == InputType.TOUCH_UP) {
            if (this.board.isInBoard(input.getX(), input.getY())) {          //Input en la zona del tablero
                engine.getAudio().playSound(this.sound);
                this.lives -= this.board.markCell(input.getX(), input.getY(), false);

                if (this.board.checkear(input.getX(), input.getY())) {            //Checkeo de la victoria
                    GameManager.getInstance().updateCategory(this.actCategory, this.actLevel, null, -1);
                    engine.getSceneManager().changeScene(new WinScene(this.board, true, this.actCategory), engine);
                }
                if (this.lives == 0)
                    engine.getSceneManager().changeScene(new WinScene(this.board, false, this.actCategory), engine);
            } else if (this.backButton.isInButton(input.getX(), input.getY())) {   //Input boton de volver
                this.backButton.clicked(engine.getAudio());
                if (this.actCategory == 0)
                    engine.getSceneManager().changeScene(new ModeSelectionMenu(), engine);
                else
                    engine.getSceneManager().changeScene(new CategoryLevelSelectionMenu(this.actCategory), engine);

                updateCategoryInformation();
            } else if (this.recoverLive.isInButton(input.getX(), input.getY())) {   //Input boton anuncio
                engine.getAdSystem().showRewardedAd();
            }
        } else if (input.getType() == InputType.TOUCH_LONG) {     //Long touch en tablero
            if (this.board.isInBoard(input.getX(), input.getY())) {
                this.board.markCell(input.getX(), input.getY(), true);
            }
        }
    }

    public void updateCategoryInformation() {
        GameManager.getInstance().updateCategory(this.actCategory, this.actLevel - 1, this.board.getBoardState(), this.lives);
    }
}
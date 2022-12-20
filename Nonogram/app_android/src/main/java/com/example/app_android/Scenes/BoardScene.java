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

    private int dim_w, dim_h, numberLevel;
    private String path, level;

    private Board board;
    private int lives, actLevel, actCategory;

    private Button backButton;
    private Button coinIndicator;
    private Button recoverLive;
    private int livesPosX, livesPosY;

    private String sound, liveImage, noLiveImage;

    public BoardScene(int w, int h) {
        this.dim_w = w;
        this.dim_h = h;
        this.numberLevel = -1;
    }

    public BoardScene(String path, int file, int category) {
        this.path = path;
        this.numberLevel = file;
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
        this.backButton = new Button(0, 0, 0, 0, "Back", Resources.IMAGE_BACK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.recoverLive = new Button(0, 0, 0, 0, "Recover\n live", Resources.IMAGE_BACK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.coinIndicator = new Button(0, 0, 0 ,0, Integer.toString(GameManager.getInstance().getCoins()),
                Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE_MEDIUM, "", false);

        rearrange(engRef);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {
        if (engRef.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait(engRef);
        else if (engRef.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape(engRef);
    }

    @Override
    public void update(double deltaTime, EngineAndroid engine) {
        if (engine.getAdSystem().hasRewardBeenGranted())
            this.lives = this.lives == MAX_LIVES ? MAX_LIVES : this.lives + 1;

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

        int getW = GameManager.getInstance().getWidth();
        int w = getW / 9;
        for (int i = MAX_LIVES; i > 0; i--) {
            String imName;
            if (i > this.lives)
                imName = this.noLiveImage;
            else
                imName = this.liveImage;
            renderMng.drawImage(livesPosX + (w * (MAX_LIVES - i)), livesPosY, w, w, imName);
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
                    engine.getSceneManager().changeScene(new WinScene(this.board, true, this.actCategory, this.numberLevel), engine);
                }
                if (this.lives == 0)
                    engine.getSceneManager().changeScene(new WinScene(this.board, false, this.actCategory, this.numberLevel), engine);
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

    private void arrangePortrait(EngineAndroid engRef) {
        int w = GameManager.getInstance().getWidth() / 3;
        int h = GameManager.getInstance().getHeight() / 12;
        this.backButton.setPosition(2 * w / 5, h / 2);
        this.backButton.setSize(w, h);
        this.backButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.coinIndicator.setPosition(8 * w / 5, h * 2);
        this.coinIndicator.setSize(w, h);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.recoverLive.setPosition(8 * w / 5, h / 2);
        this.recoverLive.setSize(w, h);
        this.recoverLive.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
        this.livesPosX = 2 * GameManager.getInstance().getWidth() / 15;
        this.livesPosY = GameManager.getInstance().getHeight() / 6;

        this.board.calcCellSize(engRef);
        this.board.setPos((GameManager.getInstance().getWidth() - board.getWidthInPixels()) / 2,
        (int)((GameManager.getInstance().getHeight() / 0.75f) - board.getHeightInPixels()) / 2);
    }

    private void arrangeLandscape(EngineAndroid engRef) {
        int w = GameManager.getInstance().getWidth() / 3;
        int h = GameManager.getInstance().getHeight() / 12;
        this.backButton.setPosition(2 * w / 5, h / 2);
        this.backButton.setSize(w, h);
        this.backButton.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.coinIndicator.setPosition(8 * w / 5, h * 2);
        this.coinIndicator.setSize(w, h);
        this.coinIndicator.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));

        this.recoverLive.setPosition(8 * w / 5, h / 2);
        this.recoverLive.setSize(w, h);
        this.recoverLive.setColor(GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal()));
        this.livesPosX = 2 * GameManager.getInstance().getWidth() / 15;
        this.livesPosY = GameManager.getInstance().getHeight() / 6;

        this.board.calcCellSize(engRef);
        this.board.setPos((GameManager.getInstance().getWidth() - board.getWidthInPixels()) / 2,
                (int)((GameManager.getInstance().getHeight() / 0.75f) - board.getHeightInPixels()) / 2);
    }
}
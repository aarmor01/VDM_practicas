package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Label;
import com.example.app_android.Resources;
import com.example.app_android.Objects.Board;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.IntentSystemAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class WinScene implements IScene {
    private Board board;
    private boolean victory;
    private int category, level;

    private Label winLabel;

    private Button backButton;
    private Button coinsButton;
    private Button shareButton;

    public WinScene(Board b, boolean win, int categ, int level){
        this.board = b;
        this.level = level;
        this.victory = win;
        this.category = categ;
    }

    @Override
    public String getId() { return "WinScene"; }

    @Override
    public void init(EngineAndroid engine) {
        // what coins to add
        int coins = this.victory ? (this.board.getCols() * this.board.getRows()) / 2 : 0;
        GameManager.getInstance().addCoins(coins);

        // title
        this.winLabel = new Label(0, 0, this.victory ? "¡¡Victoria!!" : "Derrota :(",
                Resources.FONT_EXO_REGULAR_BIG);

        // buttons
        this.backButton = new Button(0, 0, 0, 0, "Back", Resources.IMAGE_BACK_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.coinsButton = new Button(0, 0, 0, 0, "+ " + coins, Resources.IMAGE_COIN,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);
        this.shareButton = new Button(0, 0, 0, 0, "Share", Resources.IMAGE_TWITTER_BUTTON,
                Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON);

        rearrange(engine);
    }

    @Override
    public void rearrange(EngineAndroid engine) {
        if (engine.getOrientation() == EngineAndroid.Orientation.PORTRAIT)
            arrangePortrait(engine);
        else if (engine.getOrientation() == EngineAndroid.Orientation.LANDSCAPE)
            arrangeLandscape(engine);
    }

    @Override
    public void update(double deltaTime, EngineAndroid engine) {

    }

    @Override
    public void render(RenderAndroid renderer) {
        // render label
        this.winLabel.render(renderer);

        // render buttons
        this.backButton.render(renderer);
        this.coinsButton.render(renderer);

        // render solved board
        if (this.victory) {
            this.board.renderWin(renderer);
            this.shareButton.render(renderer);
        }
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if(input.getType() == InputType.TOUCH_UP && this.backButton.isInButton(input.getX(), input.getY())){
            if(this.category == 0)
                engine.getSceneManager().changeScene(new SelectionMenu(), engine);
            else
                engine.getSceneManager().changeScene(new CategoryLevelSelectionMenu(this.category), engine);
            this.backButton.clicked(engine.getAudio());
        }
        else if (input.getType() == InputType.TOUCH_UP && this.shareButton.isInButton(input.getX(), input.getY())){
            if (this.victory){
                String text = " ";
                if (level == -1)
                    text =  "I just won a super awesome random level on Nonograms, I dare you to beat it as well!";
                else
                    text = "I just won level " + level + " on Nonograms, I dare you to beat it as well!";
                engine.getIntentSystem().share(IntentSystemAndroid.SocialNetwork.TWITTER, text);
            }
        }
    }

    private void arrangePortrait(EngineAndroid engRef) {
        // label
        this.winLabel.setPos(GameManager.getInstance().getWidth() / 2, GameManager.getInstance().getHeight() / 6);

        // buttons
        int w = GameManager.getInstance().getWidth() / 4;
        int h = GameManager.getInstance().getHeight();
        this.backButton.setPosition(0, (int)(h * 7.25 / 8));
        this.backButton.setSize(w, h / 12);
        this.shareButton.setPosition(3 * w / 2, (int)(h * 7.25 / 8));
        this.shareButton.setSize(w, h / 12);
        this.coinsButton.setPosition(3 * w, (int)(h * 7.25 / 8));
        this.coinsButton.setSize(w, h / 12);


        this.board.calcCellSize(engRef);
        this.board.setPos((GameManager.getInstance().getWidth() - board.getWidthInPixels()) / 2,
                (int)((GameManager.getInstance().getHeight() / 0.75f) - board.getHeightInPixels()) / 2);
    }

    private void arrangeLandscape(EngineAndroid engRef) {
        // label
        this.winLabel.setPos(GameManager.getInstance().getWidth() / 2, GameManager.getInstance().getHeight() / 6);

        // buttons
        int w = GameManager.getInstance().getWidth() / 4;
        int h = GameManager.getInstance().getHeight();
        this.backButton.setPosition(0, (int)(h * 7.25 / 8));
        this.backButton.setSize(w, h / 12);
        this.shareButton.setPosition(3 * w / 2, (int)(h * 7.25 / 8));
        this.shareButton.setSize(w, h / 12);
        this.coinsButton.setPosition(3 * w, (int)(h * 7.25 / 8));
        this.coinsButton.setSize(w, h / 12);

        this.board.calcCellSize(engRef);
        this.board.setPos((GameManager.getInstance().getWidth() - board.getWidthInPixels()) / 2,
                (int)((GameManager.getInstance().getHeight() / 0.75f) - board.getHeightInPixels()) / 2);
    }
}

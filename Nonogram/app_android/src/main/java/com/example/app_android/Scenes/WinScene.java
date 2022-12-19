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

public class WinScene implements IScene {
    private Board board;
    private boolean victory;
    private int category;

    private String winText;
    private String winFont;

    private Button backButton;
    private Button coinsButton;

    public WinScene(Board b, boolean win, int categ){
        this.board = b;
        this.victory = win;
        this.category = categ;
    }

    @Override
    public String getId() { return "WinScene"; }

    @Override
    public void init(EngineAndroid engRef) {
        // title
        this.winFont = Resources.FONT_EXO_REGULAR_BIG;
        this.winText = this.victory ? "¡¡Victoria!!" : "Derrota :(";

        // what coins to add
        int coins = this.victory ? (this.board.getWidth() * this.board.getHeight()) / 2 : 0;
        GameManager.getInstance().addCoins(coins);

        // buttons
        int w = engRef.getRender().getWidth() / 4;
        int h = engRef.getRender().getHeight();
        this.backButton = new Button(w / 2, h * 7 / 8, w, h / 12, "Back",
                Resources.IMAGE_BACK_BUTTON, Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
        this.coinsButton = new Button(5 * w / 2, h * 7 / 8, w, h / 12, "+ " + coins,
                Resources.IMAGE_COIN, Resources.FONT_SIMPLY_SQUARE_MEDIUM, Resources.SOUND_BUTTON, false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        // render text
        renderMng.setColor(0xFF000000);
        renderMng.setFont(this.winFont);
        int w = renderMng.getTextWidth(this.winFont, this.winText);
        renderMng.drawText((renderMng.getWidth() - w) / 2, renderMng.getHeight() / 6, this.winText);

        // render solved board
        if (this.victory)
            this.board.renderWin(renderMng);

        // render buttons
        this.backButton.render(renderMng);
        this.coinsButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engRef) {
        if(input.getType() == InputType.TOUCH_UP && this.backButton.isInButton(input.getX(), input.getY())){
            if(this.category == 0)
                engRef.getSceneManager().changeScene(new SelectionMenu(), engRef);
            else
                engRef.getSceneManager().changeScene(new CategoryLevelSelectionMenu(this.category), engRef);
            this.backButton.clicked(engRef.getAudio());
        }
    }
}

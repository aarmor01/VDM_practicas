package com.example.app_android.Scenes;

import com.example.app_android.GameManager;
import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class LevelHistorySelectionMenu implements IScene {
    static int LEVELS_PER_CATEGORY = 20;

    Button[] levelSelecBut;
    Button backButton;
    Button coinIndicator;
    String font;
    String route;
    int lastUnlocked, category;

    //private EngineAndroid engRef;

    public LevelHistorySelectionMenu(String r, int cat){
        route = r;
        category = cat;
        lastUnlocked = GameManager.getInstance().getLevelUnlocked(category) + 1;
    }

    @Override
    public String getId(){return "LevelHistorySelectionMenu";}

    @Override
    public void init(EngineAndroid engRef) {
        font = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);
        int x = engRef.getRender().getWidth()/5;
        int y = engRef.getRender().getHeight()/4;
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        levelSelecBut = new Button[LEVELS_PER_CATEGORY];

        for(int i=0; i<LEVELS_PER_CATEGORY; i++){
            String im = "";
            if(i >= lastUnlocked)
                im = engRef.getRender().loadImage("./assets/images/lock.png");

            levelSelecBut[i] = new Button(x/2 + x*(i%4), y + (y/2 * (i/4)), x-x/10, x-x/10, "Lvl " + (i + 1), im, font, btAudio, false);
        }

        int getW = engRef.getRender().getWidth();
        backButton = new Button(getW/8, y/4, getW/4, (y - y/4)/2, "Back",
                engRef.getRender().loadImage("./assets/images/backbutton.png"), font, btAudio, false);
        coinIndicator = new Button(5* getW/8, y/4, getW/4, (y - y/4)/2, Integer.toString(GameManager.getInstance().getCoins()),
                engRef.getRender().loadImage("./assets/images/coin.png"), font, "", false);
    }

    @Override
    public void rearrange(EngineAndroid engRef) {

    }

    @Override
    public void update(double deltaTime) {
        lastUnlocked = GameManager.getInstance().getLevelUnlocked(category) + 1;
        for(int i=0; i<lastUnlocked; i++){
            levelSelecBut[i].setImage("");
        }
        coinIndicator.setText(Integer.toString(GameManager.getInstance().getCoins()));
    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        for(int i=0; i<LEVELS_PER_CATEGORY; i++){
            levelSelecBut[i].render(renderMng);
        }
        backButton.render(renderMng);
        coinIndicator.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input, EngineAndroid engine) {
        if (input.getType() == InputType.TOUCH_UP) {
            int x = 0;
            int i = 0;
            while(x == 0 && i<lastUnlocked){
                if(levelSelecBut[i].isInButton(input.getX(), input.getY())){
                    x = i + 1;
                }
                i++;
            }

            if(backButton.isInButton(input.getX(), input.getY())){
                engine.getSceneManager().changeScene(new ModeSelectionMenu(), engine);
                backButton.clicked(engine.getAudio());
            }

            if(x != 0){
                levelSelecBut[0].clicked(engine.getAudio());
                engine.getSceneManager().changeScene(new BoardScene(route , x, category), engine);
            }
        }
    }
}

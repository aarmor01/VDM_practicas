package com.example.app_android;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.FontType;
import com.example.engine_android.IScene;
import com.example.engine_android.InputAndroid;
import com.example.engine_android.InputType;
import com.example.engine_android.RenderAndroid;

public class LevelHistorySelectionMenu implements IScene {
    static int LEVELS_PER_CATEGORY = 20;

    Button[] levelSelecBut;
    Button backButton;
    String font;
    String route;
    int lastUnlocked, category;

    private EngineAndroid engRef;

    public LevelHistorySelectionMenu(String r, int cat){
        route = r;
        category = cat;
        lastUnlocked = GameManager.getInstance().getLevelUnlocked(0) + 1;
    }

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        font = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);
        int x = engRef.getRender().getWidth()/5;
        int y = engRef.getRender().getHeight()/4;
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        levelSelecBut = new Button[LEVELS_PER_CATEGORY];

        for(int i=0; i<LEVELS_PER_CATEGORY; i++){
            if(i < lastUnlocked)
                levelSelecBut[i] = new Button(x/2 + x*(i%4), y + (y/2 * (i/4)), x-x/10, x-x/10, "Lvl " + (i + 1), "", font, btAudio);
            else
                levelSelecBut[i] = new Button(x/2 + x*(i%4), y + (y/2 * (i/4)), x-x/10, x-x/10, "Abobole", "", font, btAudio);
        }

        backButton = new Button(engRef.getRender().getWidth()/4, y/4, engRef.getRender().getWidth()/2, (y - y/4)/2, "Back", "", font, btAudio);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        for(int i=0; i<LEVELS_PER_CATEGORY; i++){
            levelSelecBut[i].render(renderMng);
        }
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
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
                engRef.getSceneManager().popScene();
                backButton.clicked(engRef.getAudio());
            }

            if(x != 0){
                levelSelecBut[0].clicked(engRef.getAudio());
                engRef.getSceneManager().pushScene(new BoardScene(route , x, category));
            }
        }
    }
}

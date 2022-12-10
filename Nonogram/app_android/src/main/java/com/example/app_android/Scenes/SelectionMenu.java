package com.example.app_android.Scenes;

import com.example.app_android.Objects.Button;

import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.IScene;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class SelectionMenu implements IScene {

    Button t4x4;
    Button t5x5;
    Button t5x10;
    Button t8x8;
    Button t10x10;
    Button t10x15;
    Button backButton;
    String font;

    private EngineAndroid engRef;

    @Override
    public void init(EngineAndroid engine) {
        engRef = engine;
        font = engRef.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, engRef.getRender().getWidth() / 22);
        int x = engRef.getRender().getWidth()/3;
        int y = engRef.getRender().getHeight()/6;
        String btAudio = engRef.getAudio().loadSound("./assets/sounds/button.wav", 1);
        t4x4 = new Button(x / 4, y*2, x/2, x/2, "4x4", "", font, btAudio);
        t5x5 = new Button(x * 5 / 4, y*2, x/2, x/2, "5x5", "", font, btAudio);
        t5x10 = new Button(x * 9 / 4, y*2, x/2, x/2, "10x5", "", font, btAudio);
        t8x8 = new Button(x / 4, y*3, x/2, x/2, "8x8", "", font, btAudio);
        t10x10 = new Button(x * 5 / 4, y*3, x/2, x/2, "10x10", "", font, btAudio);
        t10x15 = new Button(x * 9 / 4, y*3, x/2, x/2, "15x10", "", font, btAudio);
        backButton = new Button(x, y*5, x, y/3, "Back", "", font, btAudio);
    }

    @Override
    public String getId(){return "Selectionmenu";}

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000);
        t4x4.render(renderMng);
        t5x5.render(renderMng);
        t5x10.render(renderMng);
        t8x8.render(renderMng);
        t10x10.render(renderMng);
        t10x15.render(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(InputAndroid input) {
        if (input.getType() == InputType.TOUCH_UP) {
            int x = 0, y = 0;
            if (t4x4.isInButton(input.getX(), input.getY())){
                x=4;
                y=4;
            }
            else if(t5x5.isInButton(input.getX(), input.getY())){
                x=5;
                y=5;
            }
            else if(t5x10.isInButton(input.getX(), input.getY())){
                x=10;
                y=5;
            }
            else if(t8x8.isInButton(input.getX(), input.getY())){
                x=8;
                y=8;
            }
            else if(t10x10.isInButton(input.getX(), input.getY())){
                x=10;
                y=10;
            }
            else if(t10x15.isInButton(input.getX(), input.getY())){
                x=15;
                y=10;
            }
            else if(backButton.isInButton(input.getX(), input.getY())){
                engRef.getSceneManager().popScene();
                backButton.clicked(engRef.getAudio());
            }

            if(x != 0){
                t4x4.clicked(engRef.getAudio());
                engRef.getSceneManager().pushScene(new BoardScene(x, y));
            }
        }
    }
}
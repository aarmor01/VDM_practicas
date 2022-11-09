package com.example.nonogram;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.FontType;
import com.example.engine_common.shared.InputType;

public class WinScene implements IScene {
    private Board board;
    private String winText;
    private String font;
    private Button backButton;

    private IEngine engRef;

    public WinScene(Board b){
        board = b;
    }

    @Override
    public void init(IEngine engine) {
        engRef = engine;
        winText = "¡¡Victoria!!";
        font = engRef.getRender().loadFont("./assets/fonts/arial.ttf", FontType.DEFAULT, 50);
        backButton = new Button((engRef.getRender().getWidth()-engRef.getRender().getWidth()/5)/2, engRef.getRender().getHeight()*7/8, engRef.getRender().getWidth()/5, engRef.getRender().getHeight()/12, "Back", "", font);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(IRender renderMng) {
        renderMng.setColor(0xFF000000);
        renderMng.setFont(font);
        int w = renderMng.getTextWidth(font, winText);
        renderMng.drawText((renderMng.getWidth()-w)/2, renderMng.getHeight()/6, winText);
        board.renderWin(renderMng);
        backButton.render(renderMng);
    }

    @Override
    public void handleInput(IInput input) {
        if(input.getType() == InputType.TOUCH_UP && backButton.isInBUtton(input.getX(), input.getY())){
            while(engRef.getSceneManager().getStackSize() > 1)
                engRef.getSceneManager().popScene();
        }
    }
}

package com.example.app_android.Objects;

import com.example.app_android.GameManager;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class Label {
    private String text;
    private int posX, posY;
    private String font;

    public Label(String tx, int x, int y, String f, EngineAndroid engRef) {
        text = tx;
        posX = x;
        posY = y;
        font = f;
    }

    public void render(RenderAndroid renderMng){
        renderMng.setColor(0xFF000000);
        renderMng.setFont(this.font);
        int textWidth = renderMng.getTextWidth(this.font, this.text)/2;
        renderMng.drawText(posX - textWidth, posY, text);
    }

    public void setPos(int x, int y){
        posX = x;
        posY = y;
    }
}

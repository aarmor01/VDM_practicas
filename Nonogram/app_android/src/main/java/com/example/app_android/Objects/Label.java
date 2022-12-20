package com.example.app_android.Objects;

import com.example.app_android.GameManager;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class Label {
    private String text;
    private int posX, posY;
    private String font;
    private int textWidth;

    public Label(String tx, int x, int y, String f, EngineAndroid engRef) {
        text = tx;
        posX = x;
        posY = y;
        font = f;
        textWidth = engRef.getRender().getTextWidth(font, text);
    }

    public void render(RenderAndroid renderMng){
        renderMng.setColor(0xFF000000);
        renderMng.setFont(this.font);
        int textWidth = renderMng.getTextWidth(this.font, this.text);
        renderMng.drawText(posX, posY, text);
    }

    public void setPos(int x, int y){
        posX = x;
        posY = y;
    }

    public int getTextW(){ return textWidth; }
}

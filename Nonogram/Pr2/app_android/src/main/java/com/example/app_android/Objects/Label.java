package com.example.app_android.Objects;

import com.example.engine_android.Modules.RenderAndroid;

public class Label {
    private int posX, posY;
    private int color;
    private String text;
    private String font;

    public Label(int x, int y, String text, String font) {
        this.posX = x; this.posY = y;

        this.text = text;
        this.font = font;
        this.color = 0xFF000000;
    }

    public void render(RenderAndroid renderer) {
        renderer.setColor(this.color);
        renderer.setFont(this.font);
        int textWidth = renderer.getTextWidth(this.font, this.text) / 2;
        renderer.drawText(this.posX - textWidth, this.posY, this.text);
    }

    public void setPos(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public void setFont(String newFont) {
        this.font = newFont;
    }

    public void setColor(int newColor) {
        this.color = newColor;
    }
}

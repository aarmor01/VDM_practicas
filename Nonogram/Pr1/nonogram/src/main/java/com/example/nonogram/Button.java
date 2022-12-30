package com.example.nonogram;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IRender;

public class Button {
    private int posX, posY;
    private int width, height;
    private String text;
    private String font;
    private String image;
    private String sound;

    public Button(int x, int y, int w, int h, String tx, String im, String f, String s) {
        this.posX = x; this.posY = y;
        this.width = w; this.height = h;

        this.text = tx;
        this.font = f;
        this.image = im;
        this.sound = s;
    }

    public void render(IRender renderer) {
        // background color
        renderer.setColor(0xFFCCCCCC);
        renderer.setFont(this.font);
        renderer.drawRectangle(this.posX, this.posY, this.width, this.height, true);

        // draw image
        int x = this.posX;
        if (this.image != "") {
            x += this.height - 2;
            renderer.drawImage(this.posX + 1, this.posY + 1, this.height - 2,
                    this.height - 2, this.image);
        }

        // draw text
        int textWidth = renderer.getTextWidth(this.font, this.text);
        renderer.setColor(0xFF000000);
        renderer.drawText(x + (this.width - (x - this.posX)) / 2 - textWidth / 2,
                this.posY + this.height / 2 + renderer.getTextHeight(this.font) / 2, this.text);
    }

    public void clicked(IAudio soundMng) {
        soundMng.playSound(this.sound);
    }

    public boolean isInButton(int x, int y) {
        return x > this.posX && x < this.posX + this.width && y > this.posY && y < this.posY + this.height;
    }
}

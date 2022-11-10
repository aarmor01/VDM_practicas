package com.example.nonogram;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IRender;

public class Button {
    private String text;
    private String font;
    private String image;
    private int posX, posY;
    private int width, height;
    private String sound;

    public Button(int x, int y, int w, int h, String tx, String im, String f, String s){
        posX = x;
        posY = y;
        width = w;
        height = h;
        text = tx;
        font = f;
        image = im;
        sound = s;
    }

    public void render(IRender renderMng){
        renderMng.setColor(0xFFCCCCCC);
        renderMng.setFont(font);
        renderMng.drawRectangle(posX, posY, width, height, true);
        int x = posX;
        if(image != ""){
            x += height-2;
            renderMng.drawImage(posX + 1, posY+1, height-2, height-2, image);
        }

        int lg = renderMng.getTextWidth(font, text);

        renderMng.setColor(0xFF000000);
        renderMng.drawText(x + (width - (x-posX))/2 - lg/2, posY + height/2 + renderMng.getTextHeight(font)/2, text);
    }
    public boolean isInButton(int x, int y){
        return x > posX && x < posX + width && y > posY && y < posY + height;
    }

    public void clicked(IAudio soundMng){
        soundMng.playSound(sound);
    }
}

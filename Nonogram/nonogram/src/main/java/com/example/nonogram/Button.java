package com.example.nonogram;

import com.example.engine_common.interfaces.IRender;

public class Button {
    private String text;
    private String font;
    private String image;
    private int posX, posY;
    private int width, height;

    public Button(int x, int y, int w, int h, String tx, String im, String f){
        posX = x;
        posY = y;
        width = w;
        height = h;
        text = tx;
        font = f;
        image = im;
    }

    public void render(IRender renderMng){
        renderMng.setColor(0xFF000000);
        renderMng.drawRectangle(posX, posY, width, height, false);
        int x = posX;
        if(image != ""){
            x += height-2;
            renderMng.drawImage(posX + 1, posY+1, height-2, height-2, image);
        }

        int lg = renderMng.getTextWidth(font, text);
        renderMng.drawText(x + (width - (x-posX))/2 - lg/2, posY + height/2 + renderMng.getTextHeight(font)/2, text);
    }

    public boolean isInBUtton(int x, int y){
        return x > posX && x < posX + width && y > posY && y < posY + height;
    }
}

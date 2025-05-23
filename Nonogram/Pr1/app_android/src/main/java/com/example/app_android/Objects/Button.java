package com.example.app_android.Objects;

import com.example.app_android.GameManager;
import com.example.engine_android.Modules.AudioAndroid;
import com.example.engine_android.Modules.RenderAndroid;

public class Button {
    private String text;
    private String font;
    private String image;
    private int posX, posY;
    private int width, height;
    private int color;
    private String sound;
    private boolean mode;

    public Button(int x, int y, int w, int h, String tx, String im, String f, String s, boolean m){
        posX = x;
        posY = y;
        width = w;
        height = h;
        text = tx;
        font = f;
        image = im;
        sound = s;
        color = GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal());
        mode = m;
    }

    public void render(RenderAndroid renderMng){
        renderMng.setColor(color);
        renderMng.setFont(font);
        renderMng.drawRectangle(posX, posY, width, height, true);
        int x = posX;
        if(image != ""){
            x += height-2;
            renderMng.drawImage(posX + 1, posY+1, height-2, height-2, image);
        }

        int lg = renderMng.getTextWidth(font, text);

        String[] splTx = text.split("\n");
        renderMng.setColor(0xFF000000);
        renderMng.drawText(x + (width - (x-posX))/2 - lg/2, posY + height / 2 + (renderMng.getTextHeight(font) / splTx.length) / 2, text);
    }
    public boolean isInButton(int x, int y){
        return x > posX && x < posX + width && y > posY && y < posY + height;
    }
    public void setImage(String tx){ image = tx; }
    public void setText(String tx){ text = tx; }
    public void clicked(AudioAndroid soundMng){
        soundMng.playSound(sound);
    }

    public void setPosition(int x, int y){
        posX = x;
        posY = y;
    }

    public void setSize(int w, int h){
        width = w;
        height = h;
    }

    public void setColor(int c){ color = c; }
}

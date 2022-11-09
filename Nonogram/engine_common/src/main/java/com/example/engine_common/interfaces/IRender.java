package com.example.engine_common.interfaces;

import com.example.engine_common.shared.FontType;

import java.io.IOException;

public interface IRender {
    public String loadImage(String filePath);
    public String loadFont(String filePath, FontType type, int size);

    public void setColor(int hexColor);
    public void setFont(String id);

    public void drawLine(int og_x, int og_y, int dst_x, int dst_y);
    public void drawRectangle(int x, int y, int width, int height, boolean fill);
    public void drawCircle(int x, int y, int r, boolean fill);

    public void drawImage(int x, int y, int width, int height, String imageID);
    public void drawText(int x, int y, String text);

    public int getWidth();
    public int getHeight();

    public int getTextWidth(String fontID, String text);
}
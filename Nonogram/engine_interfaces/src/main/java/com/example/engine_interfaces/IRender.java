package com.example.engine_interfaces;

import java.awt.Color;

public interface IRender {
    public IImage newImage();
    public IFont newFont();

    public void setResolution();
    public void setColor();
    public void setFont();

    public void drawImage();
    public void drawRectangle(int x, int y, int width, int height, boolean fill);
    public void drawLine(int og_x, int og_y, int dst_x, int dst_y);
    public void drawText(int x, int y, String text, IFont font);
    public void drawCircle(int x, int y, int r);

    public int getWindowWidth();
    public int getWindowHeight();
}
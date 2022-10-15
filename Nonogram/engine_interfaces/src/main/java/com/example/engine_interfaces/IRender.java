package com.example.engine_interfaces;

import java.awt.Color;

public interface IRender {
    public IImage newImage();
    public IFont newFont();

    public void setResolution();
    public void setColor();
    public void setFont();

    public void drawImage();
    public void drawRectangle();
    public void fillRectangle();
    public void drawLine();
    public void drawText();
    public void drawCircle(int x, int y, int r);

    public int getWindowWidth();
    public int getWindowHeight();
}
package com.example.engine_interfaces;

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
}
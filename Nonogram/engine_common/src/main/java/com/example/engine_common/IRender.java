package com.example.engine_common;

public interface IRender {
    public String loadImage(String filePath);
    public String loadFont(String filePath);

    public void setResolution();
    public void setColor();
    public void setFont();

    public void drawImage(int x, int y, String imageID);
    public void drawText(int x, int y, String text, String fontID);

    public void drawRectangle(int x, int y, int width, int height, boolean fill);
    public void drawLine(int og_x, int og_y, int dst_x, int dst_y);
    public void drawCircle(int x, int y, int r);

    public int getWindowWidth();
    public int getWindowHeight();
}
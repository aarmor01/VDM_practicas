package com.example.engine_interfaces;

public interface IRender {
    public int getWidth();
    public int getHeight();

    public void run();

    public void update(double deltaTime);
    //public void setScene(MyScene scene);

    public void renderCircle(float x, float y, float r);
    //renderText o Fuente hay que hacer uno común

    public void render();
    public void pause();

    public void resume();

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

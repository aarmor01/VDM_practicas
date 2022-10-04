package com.example.engine_interfaces;

public interface IRender {
    public int getWidth();
    public int getHeight();

    public void run();

    void update(double deltaTime);
    //public void setScene(MyScene scene);

    void renderCircle(float x, float y, float r);
    //renderText o Fuente hay que hacer uno común

    void render();
    public void resume();
    public void pause();






}

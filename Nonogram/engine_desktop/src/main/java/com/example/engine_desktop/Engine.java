package com.example.engine_desktop;

import com.example.engine_interfaces.*;

import javax.swing.JFrame;

public class Engine implements IEngine, Runnable {

    // thread variables
    private Thread renderThread;
    private boolean running;

    // engine variables
    Render myRender;
    IScene currScene;

    public void init(JFrame myWindow, IScene startScene) {
        myRender = new Render();
        currScene = startScene;

        myRender.init(myWindow);
    }

    @Override
    public IRender getRender() {
        return null;
    }

    @Override
    public IAudio getAudio() {
        return null;
    }

    @Override
    public IInput getInput() {
        return null;
    }

    @Override
    public IScene getScene() {
        return null;
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while (this.running && this.myRender.getWindowWidth() == 0);
        // Espera activa. Sería más elegante al menos dormir un poco.


//        long informePrevio = lastFrameTime; // Informes de FPS
//        int frames = 0;

        long lastFrameTime = System.nanoTime();

        // Bucle de juego principal.
        while (running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS [DEBUG]
//            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
//            this.update(elapsedTime);
//            if (currentTime - informePrevio > 1000000000l) {
//                long fps = frames * 1000000000l / (currentTime - informePrevio);
//                System.out.println("" + fps + " fps");
//                frames = 0;
//                informePrevio = currentTime;
//            }
//            ++frames;

            this.currScene.update(nanoElapsedTime);
            this.myRender.render(currScene);
        }
    }
}
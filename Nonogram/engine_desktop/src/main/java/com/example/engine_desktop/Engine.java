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
        return myRender;
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
            double deltaTime = (double)nanoElapsedTime / 1.0E9;

            // Informe de FPS [DEBUG]
//
//            this.update(elapsedTime);
//            if (currentTime - informePrevio > 1000000000l) {
//                long fps = frames * 1000000000l / (currentTime - informePrevio);
//                System.out.println("" + fps + " fps");
//                frames = 0;
//                informePrevio = currentTime;
//            }
//            ++frames;

            this.currScene.update(deltaTime);
            this.myRender.render(currScene);
        }
    }

    public void resume() {
        if (!this.running) {
            this.running = true;

            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    public void pause() {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            }
        }
    }
}
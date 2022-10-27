package com.example.engine_desktop;

import com.example.engine_common.*;

import javax.swing.JFrame;

public class EngineDesktop implements IEngine, Runnable {

    // thread variables
    private Thread renderThread;
    private boolean running;

    // engine variables
    RenderDesktop myRenderDesktop;
    SceneManager mySceneManager;

    public void init(JFrame myWindow) {
        // add listeners

        myRenderDesktop = new RenderDesktop();
        mySceneManager = new SceneManager();

        myRenderDesktop.init(myWindow);
    }

    public void setStartScene(IScene startScene) {
        this.mySceneManager.pushScene(startScene);
    }

    @Override
    public IRender getRender() {
        return myRenderDesktop;
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

        while (this.running && this.myRenderDesktop.getWindowWidth() == 0);

        long lastFrameTime = System.nanoTime();

        // Bucle de juego principal.
        if(running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double deltaTime = (double)nanoElapsedTime / 1.0E9;

            // update
            this.mySceneManager.currentScene().update(deltaTime);

            // render
            do {
                this.myRenderDesktop.prepareFrame();
                this.mySceneManager.currentScene().render(this.myRenderDesktop);
                this.myRenderDesktop.finishFrame();
            } while(this.myRenderDesktop.swapBuffer());
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
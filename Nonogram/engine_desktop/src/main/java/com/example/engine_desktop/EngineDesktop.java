package com.example.engine_desktop;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.InputManager;
import com.example.engine_common.shared.InputType;
import com.example.engine_common.shared.SceneManager;

import javax.swing.JFrame;

public class EngineDesktop implements IEngine, Runnable {

    // thread variables
    private Thread renderThread;
    private boolean running;

    // engine variables
    private RenderDesktop myRenderDesktop;
    private SceneManager mySceneManager;
    private InputManager myInputManager;

    public EngineDesktop(JFrame myWindow) {
        myRenderDesktop = new RenderDesktop();
        mySceneManager = new SceneManager();
        myInputManager = new InputManager();

        myRenderDesktop.init(myWindow);

        // add listeners window
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
    public SceneManager getSceneManager() {
        return mySceneManager;
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        while (this.running && this.myRenderDesktop.getWidth() == 0);

        long lastFrameTime = System.nanoTime();

        // Bucle de juego principal.
        if(this.running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double deltaTime = (double) nanoElapsedTime / 1.0E9;

            // handle input
//            IInput input;
//            for ((input = myInputManager.getInput()) == null)

            // update
            this.mySceneManager.currentScene().update(deltaTime);

            // render
            do {
                this.myRenderDesktop.prepareFrame();
                this.mySceneManager.currentScene().render(this.myRenderDesktop);
                this.myRenderDesktop.finishFrame();
            } while (this.myRenderDesktop.swapBuffer());
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
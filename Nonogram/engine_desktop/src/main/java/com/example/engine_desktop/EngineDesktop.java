package com.example.engine_desktop;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.InputManager;
import com.example.engine_common.shared.InputType;
import com.example.engine_common.shared.SceneManager;

import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

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
        myWindow.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
//                IInput ip = new IInput()
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
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

        // Bucle de juego principal.
        long currentTime = System.currentTimeMillis();
        while (this.running) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            //System.out.println(deltaTime);
            currentTime += deltaTime;

            // handle input
//            IInput input;
//            for ((input = myInputManager.getInput()) == null)

            // update
            this.mySceneManager.currentScene().update(deltaTime / 1000.0);

            // render
            do {
                this.myRenderDesktop.prepareFrame();
                this.mySceneManager.currentScene().render(this.myRenderDesktop);
                this.myRenderDesktop.finishFrame();
            } while (this.myRenderDesktop.swapBuffer());

//            try {
//                Thread.sleep(16);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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

    private class InputListenerDesktop implements MouseInputListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
//            InputDesktop ip = new InputDesktop(mouseEvent.getX(), mouseEvent.getY(), )
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }
}
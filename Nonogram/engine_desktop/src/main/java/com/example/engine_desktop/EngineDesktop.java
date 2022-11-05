package com.example.engine_desktop;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.FontType;
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
    private AudioDesktop myAudioDesktop;
    private SceneManager mySceneManager;
    private InputManager myInputManager;

    public EngineDesktop(JFrame myWindow) {
        myRenderDesktop = new RenderDesktop(myWindow);
        myAudioDesktop = new AudioDesktop();
        mySceneManager = new SceneManager();
        myInputManager = new InputManager();

        // add listeners window
        myWindow.addMouseListener(new InputListenerDesktop(myInputManager));
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
            currentTime += deltaTime;

            // handle input
            while(!myInputManager.empty()){
                IInput input = myInputManager.getInput();
                this.mySceneManager.currentScene().handleInput(input);
            }

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

    @Override
    public IRender getRender() {
        return myRenderDesktop;
    }

    @Override
    public IAudio getAudio() {
        return myAudioDesktop;
    }

    @Override
    public SceneManager getSceneManager() {
        return mySceneManager;
    }

    @Override
    public InputManager getInputManager() { return myInputManager; }

    private class InputListenerDesktop implements MouseInputListener {

        InputManager iM;

        InputListenerDesktop(InputManager iM) {
            this.iM = iM;
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            System.out.println("drg");
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            InputDesktop ip = new InputDesktop(mouseEvent.getX(), mouseEvent.getY(), InputType.TOUCH_MOVE);
            this.iM.addInput((ip));
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            InputDesktop ip = new InputDesktop(mouseEvent.getX(), mouseEvent.getY(), InputType.TOUCH_DOWN);
            this.iM.addInput((ip));
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            System.out.println("clk");
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            InputDesktop ip = new InputDesktop(mouseEvent.getX(), mouseEvent.getY(), InputType.TOUCH_UP);
            this.iM.addInput((ip));
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            System.out.println("ent");
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            System.out.println("ext");
        }
    }
}
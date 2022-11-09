package com.example.engine_desktop;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;

import com.example.engine_common.shared.SceneManager;
import com.example.engine_common.shared.InputManager;
import com.example.engine_common.shared.InputType;

import java.util.LinkedList;

import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

public class EngineDesktop implements IEngine, Runnable {
    // engine variables
    private RenderDesktop myRenderDesktop;
    private AudioDesktop myAudioDesktop;
    private SceneManager mySceneManager;
    private InputManager myInputManager;

    // thread variables
    private Thread renderThread;
    private boolean running;

    public EngineDesktop(JFrame myWindow, int bg) {
        this.myRenderDesktop = new RenderDesktop(myWindow, bg);
        this.myAudioDesktop = new AudioDesktop();
        this.mySceneManager = new SceneManager(this);
        this.myInputManager = new InputManager();

        // add input listener to window
        myWindow.addMouseListener(new InputListenerDesktop(this.myInputManager, this.myRenderDesktop));
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        while (this.running && !this.myRenderDesktop.windowCreated());

        long currentTime = System.currentTimeMillis();
        while (this.running) {
            try {
                // frame time
                long deltaTime = System.currentTimeMillis() - currentTime;
                currentTime += deltaTime;

                // handle input
                LinkedList<IInput> input = myInputManager.getInput();
                while (!input.isEmpty())
                    this.mySceneManager.currentScene().handleInput(input.removeFirst());

                // update
                this.mySceneManager.currentScene().update(deltaTime / 1000.0);

                // render
                do {
                    this.myRenderDesktop.prepareFrame(0xFFFFFFFF);
                    this.mySceneManager.currentScene().render(this.myRenderDesktop);
                    this.myRenderDesktop.finishFrame();
                } while (this.myRenderDesktop.swapBuffer());
            } catch (Exception e) {
                System.err.println("Frame lost");
                e.printStackTrace();
            }
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
                } catch (Exception e) {
                    System.err.println("Thread join error");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IRender getRender() { return this.myRenderDesktop; }

    @Override
    public IAudio getAudio() { return this.myAudioDesktop; }

    @Override
    public SceneManager getSceneManager() { return this.mySceneManager; }

    @Override
    public InputManager getInputManager() { return this.myInputManager; }

    private class InputListenerDesktop implements MouseInputListener {
        InputManager iM;
        RenderDesktop rD;

        InputListenerDesktop(InputManager iM, RenderDesktop rD) {
            this.iM = iM;
            this.rD = rD;
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {}

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            int input_x = (int)Math.round((mouseEvent.getX() - this.rD.getOffsetX()) / this.rD.getScale());
            int input_y = (int)Math.round((mouseEvent.getY() - this.rD.getOffsetY()) / this.rD.getScale());

            if(input_x < 0 || input_y < 0 || input_x > this.rD.getWidth() || input_y > this.rD.getHeight())
                return;

            InputDesktop ip = new InputDesktop(input_x, input_y, InputType.TOUCH_MOVE);
            this.iM.addInput((ip));
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            int input_x = (int)Math.round((mouseEvent.getX() - this.rD.getOffsetX()) / this.rD.getScale());
            int input_y = (int)Math.round((mouseEvent.getY() - this.rD.getOffsetY()) / this.rD.getScale());

            if(input_x < 0 || input_y < 0 || input_x > this.rD.getWidth() || input_y > this.rD.getHeight())
                return;

            InputDesktop ip = new InputDesktop(input_x, input_y, InputType.TOUCH_DOWN);
            this.iM.addInput((ip));
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {}

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            int input_x = (int)Math.round((mouseEvent.getX() - this.rD.getOffsetX()) / this.rD.getScale());
            int input_y = (int)Math.round((mouseEvent.getY() - this.rD.getOffsetY()) / this.rD.getScale());

            if(input_x < 0 || input_y < 0 || input_x > this.rD.getWidth() || input_y > this.rD.getHeight())
                return;

            InputDesktop ip = new InputDesktop(input_x, input_y, InputType.TOUCH_UP);
            this.iM.addInput((ip));
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {}

        @Override
        public void mouseExited(MouseEvent mouseEvent) {}
    }
}
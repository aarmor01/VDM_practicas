package com.example.engine_android;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import android.content.res.AssetManager;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;

import com.example.engine_common.shared.InputManager;
import com.example.engine_common.shared.InputType;
import com.example.engine_common.shared.SceneManager;

import java.util.LinkedList;

public class EngineAndroid implements IEngine, Runnable {
    // engine variables
    private RenderAndroid myRenderManager;
    private SceneManager mySceneManager;
    private InputManager myInputManager;
    private AudioAndroid myAudioManager;

    // asset manager
    private AssetManager assetManager;

    // start scene
    private IScene startScene;

    // thread variables
    private Thread renderThread;
    private boolean running;

    public EngineAndroid(SurfaceView surface, AssetManager aM, float ratio, int bgColor) {
        this.assetManager = aM;

        this.myRenderManager = new RenderAndroid(surface, this.assetManager, ratio, bgColor);
        this.myAudioManager = new AudioAndroid(this.assetManager);
        this.mySceneManager = new SceneManager(this);
        this.myInputManager = new InputManager();

        // add input listener to window
        surface.setOnTouchListener(new InputListener());
    }

    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        while(this.running && myRenderManager.getViewWidth() == 0);

        this.myRenderManager.adaptScale();
        this.mySceneManager.currentScene().init(this);
        long currentTime = System.currentTimeMillis();
        while(this.running) {
            try {
                // frame time
                long deltaTime = System.currentTimeMillis() - currentTime;
                currentTime += deltaTime;

                // handle input
                LinkedList<IInput> input = this.myInputManager.getInput();
                while (!input.isEmpty())
                    this.mySceneManager.currentScene().handleInput(input.removeFirst());

                // update
                this.mySceneManager.currentScene().update(deltaTime/1000.0f);

                // render
                while (!this.myRenderManager.surfaceValid());
                this.myRenderManager.clear();
                this.mySceneManager.currentScene().render(this.myRenderManager);
                this.myRenderManager.present();
            }
            catch (Exception e) {
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
    public IRender getRender() { return this.myRenderManager; }

    @Override
    public IAudio getAudio() { return this.myAudioManager; }

    @Override
    public SceneManager getSceneManager() { return this.mySceneManager; }

    @Override
    public InputManager getInputManager() { return this.myInputManager; }

    private class InputListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int input_y = (int)motionEvent.getY() - (myRenderManager.getViewHeight() - myRenderManager.getHeight()) / 2;
            int input_x = (int)motionEvent.getX() - (myRenderManager.getViewWidth() - myRenderManager.getWidth()) / 2;
            
            if (input_x < 0 ||  input_y < 0 || input_x > myRenderManager.getWidth() || input_y > myRenderManager.getHeight())
                return true;
            
            InputAndroid iA = new InputAndroid( input_x, input_y, InputType.values()[motionEvent.getActionMasked()], 
                    motionEvent.getActionIndex());
            if (InputType.TOUCH_DOWN == iA.getType() || InputType.TOUCH_UP == iA.getType() ||
                    InputType.TOUCH_MOVE == iA.getType())
                myInputManager.addInput(iA);
            
            return true;
        }
    }
}
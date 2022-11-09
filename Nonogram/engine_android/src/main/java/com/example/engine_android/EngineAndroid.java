package com.example.engine_android;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

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
    private Thread renderThread;
    private boolean running;
    private RenderAndroid render;
    private IScene currentScene;
    private SurfaceView renderView;
    private  AssetManager assetManager;
    private SceneManager mySceneManager;
    private InputManager myInputManager;
    private AudioAndroid myAudioManager;

    public EngineAndroid(SurfaceView s, AssetManager aM) {
        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = s;
        this.renderView.setOnTouchListener(new myTouchListener());
        this.assetManager = aM;
        this.myInputManager = new InputManager();
        this.render = new RenderAndroid(this.renderView, this.assetManager, 4.0f/6.0f);
        this.myAudioManager = new AudioAndroid(this.assetManager);
        this.mySceneManager = new SceneManager();
    }

    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        while(this.running && render.getViewWidth() == 0);

        this.render.scaleApp();
//        this.mySceneManager.currentScene().init(this);
        long currentTime = System.currentTimeMillis();

        // Bucle de juego principal.
        while(this.running) {
            try {
                //System.out.printf("Conche su madre que significa chainshaw man");
                long deltaTime = System.currentTimeMillis() - currentTime;
                currentTime += deltaTime;

                // handle input
                LinkedList<IInput> input = this.myInputManager.getInput();
                while (!input.isEmpty())
                    this.mySceneManager.currentScene().handleInput(input.removeFirst());

                this.mySceneManager.currentScene().update(deltaTime/1000.0f);

                while (!this.render.surfaceValid());
                this.render.clear();
                this.mySceneManager.currentScene().render(this.render);
                this.render.present();
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
    public IRender getRender() { return this.render; }

    @Override
    public IAudio getAudio() { return this.myAudioManager; }

    @Override
    public SceneManager getSceneManager() { return this.mySceneManager; }

    @Override
    public InputManager getInputManager() { return this.myInputManager; }

    private class myTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int input_y = (int)motionEvent.getY() - (render.getViewHeight() - render.getHeight()) / 2;
            int input_x = (int)motionEvent.getX() - (render.getViewWidth() - render.getWidth()) / 2;
            
            if (input_x < 0 ||  input_y < 0 || input_x > render.getWidth() || input_y > render.getHeight() ||)
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
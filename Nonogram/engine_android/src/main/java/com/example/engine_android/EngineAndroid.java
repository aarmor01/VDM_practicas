package com.example.engine_android;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class EngineAndroid implements Runnable {
    // engine variables
    private RenderAndroid myRenderManager;
    private SceneManager mySceneManager;
    private InputManager myInputManager;
    private AudioAndroid myAudioManager;

    // asset manager
    private AssetManager assetManager;
    private Context context;

    // start scene
    private IScene startScene;

    // thread variables
    private Thread renderThread;
    private boolean running;

    public EngineAndroid(SurfaceView surface, Context cont, float ratio, int bgColor) {
        this.context = cont;
        this.assetManager = cont.getAssets();

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

        while (this.running && myRenderManager.getViewWidth() == 0) ;

        this.myRenderManager.adaptScale();
        this.mySceneManager.currentScene().init(this);
        long currentTime = System.currentTimeMillis();
        while (this.running) {
            try {
                // frame time
                long deltaTime = System.currentTimeMillis() - currentTime;
                currentTime += deltaTime;

                // handle input
                LinkedList<InputAndroid> input = this.myInputManager.getInput();
                while (!input.isEmpty())
                    this.mySceneManager.currentScene().handleInput(input.removeFirst());

                // update
                this.mySceneManager.currentScene().update(deltaTime / 1000.0f);

                // render
                while (!this.myRenderManager.surfaceValid()) ;
                this.myRenderManager.clear();
                this.mySceneManager.currentScene().render(this.myRenderManager);
                this.myRenderManager.present();
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

    public RenderAndroid getRender() {
        return this.myRenderManager;
    }

    public AudioAndroid getAudio() {
        return this.myAudioManager;
    }

    public SceneManager getSceneManager() {
        return this.mySceneManager;
    }

    public InputManager getInputManager() {
        return this.myInputManager;
    }

    public FileInputStream openInputFile(String path) {
        FileInputStream file = null;
        try {
            file = this.context.openFileInput(path);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public FileOutputStream openOutputFile(String path) {
        FileOutputStream file = null;
        try {
            file = this.context.openFileOutput(path, Context.MODE_PRIVATE);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public void removeFile(String path) {
        this.context.deleteFile(path);
    }

    public String readText(String path, String file) {
        //Carga de archivo
        String receiveString = "";
        try {
            //Comprobar si existe en el almacenamiento interno
            FileInputStream fis = context.openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            try {
                while (bufferedReader.ready()) {
                    receiveString += bufferedReader.readLine();
                }
                inputStreamReader.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return receiveString;

        } catch (FileNotFoundException e) {
            //Si no existe, crea un nuevo archivo en almacenamiento interno como copia desde assets
            e.printStackTrace();
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(assetManager.open(path + file));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                while (bufferedReader.ready()) {
                    receiveString += bufferedReader.readLine();
                }

                inputStreamReader.close();

                return receiveString;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return null;
    }

    private class InputListener implements View.OnTouchListener {

        private int input_x_original;
        private int input_y_original;
        private boolean goneFlag;

        // input variables
        private Handler handler = new Handler();

        private Runnable mLongPressed = new Runnable() {
            @Override
            public void run() {
                goneFlag = true;
            }
        };

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int input_y = (int) motionEvent.getY() - (myRenderManager.getViewHeight() - myRenderManager.getHeight()) / 2;
            int input_x = (int) motionEvent.getX() - (myRenderManager.getViewWidth() - myRenderManager.getWidth()) / 2;

            if (input_x < 0 || input_y < 0 || input_x > myRenderManager.getWidth() || input_y > myRenderManager.getHeight())
                return true;

            InputAndroid iA = null;

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    goneFlag = false;
                    input_x_original = input_x;
                    input_y_original = input_y;
                    iA = new InputAndroid( input_x, input_y, InputType.TOUCH_DOWN,
                            motionEvent.getActionIndex());
                    myInputManager.addInput(iA);
                    handler.postDelayed(mLongPressed, 500);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacks(mLongPressed);
                    if(!goneFlag) {
                        iA = new InputAndroid( input_x, input_y, InputType.TOUCH_UP,
                                motionEvent.getActionIndex());
                        myInputManager.addInput(iA);
                        return true;
                    }
                    else {
                        iA = new InputAndroid( input_x, input_y, InputType.TOUCH_LONG,
                                motionEvent.getActionIndex());
                        myInputManager.addInput(iA);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!(Math.abs(input_x - input_x_original) <= 5 && Math.abs(input_y - input_y_original) <= 5))
                        iA = new InputAndroid( input_x, input_y, InputType.TOUCH_MOVE,
                                motionEvent.getActionIndex());
                        myInputManager.addInput(iA);
                    break;
            }
            return true;
        }
    }
}
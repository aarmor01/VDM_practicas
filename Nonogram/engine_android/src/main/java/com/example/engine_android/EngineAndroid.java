package com.example.engine_android;

import com.example.engine_android.Enums.InputType;
import com.example.engine_android.DataStructures.InputAndroid;
import com.example.engine_android.Modules.AdSystemAndroid;
import com.example.engine_android.Modules.AudioAndroid;
import com.example.engine_android.Modules.InputManager;
import com.example.engine_android.Modules.IntentSystemAndroid;
import com.example.engine_android.Modules.LightSensor;
import com.example.engine_android.Modules.RenderAndroid;
import com.example.engine_android.Modules.SceneManager;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;

public class EngineAndroid implements Runnable {
    public enum Orientation { PORTRAIT, LANDSCAPE };

    // engine modules
    private final RenderAndroid myRenderManager;
    private final SceneManager mySceneManager;
    private final InputManager myInputManager;
    private final AudioAndroid myAudioManager;
    private final AdSystemAndroid myAdSystem;
    private final IntentSystemAndroid myIntentSystem;
    private final LightSensor myLightSensor;

    // asset manager
    private final AssetManager assetManager;
    private final Context context;

    // current orientation
    private Orientation orientation;

    // thread variables
    private Thread renderThread;
    private boolean running;
    private Thread configThread;
    private boolean initialConfigurationDone;

    public EngineAndroid(SurfaceView surface, AppCompatActivity activity, float ratio, int bgColor) {
        // context
        this.context = activity.getBaseContext();
        this.assetManager = this.context.getAssets();

        // orientation (-1 because it starts PORTRAIT == 1, LANDSCAPE == 2)
        this.orientation = Orientation.values()[activity.getResources().getConfiguration().orientation - 1];

        // engine modules initialization
        this.myRenderManager = new RenderAndroid(surface, this.assetManager, ratio, bgColor);
        this.myAudioManager = new AudioAndroid(this.assetManager);
        this.mySceneManager = new SceneManager();
        this.myInputManager = new InputManager();
        this.myAdSystem = new AdSystemAndroid(activity, this.context);
        this.myIntentSystem = new IntentSystemAndroid(this.context);
        this.myLightSensor = new LightSensor(this.context);
        // add input listener to window
        surface.setOnTouchListener(new InputListener());

        // thread to generate initial configuration
        initialConfigurationDone = false;
        this.configThread = new Thread(new SurfaceAvailable(this));
        this.configThread.start();
    }

    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        // we wait for the initial configuration to end before starting the game cycle
        waitSurfaceConfiguration();

        long currentTime = System.currentTimeMillis();
        while (this.running) {
            try {
                // frame time
                long deltaTime = System.currentTimeMillis() - currentTime;
                currentTime += deltaTime;

                // handle input
                LinkedList<InputAndroid> input = this.myInputManager.getInput();
                while (!input.isEmpty())
                    this.mySceneManager.currentScene().handleInput(input.removeFirst(), this);

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
            // resume engine
            this.running = true;

            this.renderThread = new Thread(this);
            this.renderThread.start();

            // resume audio
            this.myAudioManager.playMusic();
        }
    }

    public void pause() {
        if (this.running) {
            // pause audio
            this.myAudioManager.pauseMusic();

            // pause engine
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

    public AdSystemAndroid getAdSystem() {
        return this.myAdSystem;
    }

    public IntentSystemAndroid getIntentSystem() {
        return this.myIntentSystem;
    }

    public LightSensor getLightSensor() {return this.myLightSensor;}

    // TODO: change scene init structure so we don't need this and we just return the enum in the method
    public Orientation getOrientation() { return orientation; }

    public void updateConfiguration(Configuration config) {
        // (-1 because it starts PORTRAIT == 1, LANDSCAPE == 2)
        this.orientation = Orientation.values()[config.orientation - 1];
        this.mySceneManager.currentScene().rearrange(this);
    }

    public FileInputStream openInputFile(String path) {
        FileInputStream file = null;
        try {
            file = this.context.openFileInput(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public FileOutputStream openOutputFile(String path) {
        FileOutputStream file = null;
        try {
            file = this.context.openFileOutput(path, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public void removeFile(String path) {
        this.context.deleteFile(path);
    }

    public ArrayList<String> readText(String path, String file) {
        //Carga de archivo
        ArrayList<String> receiveString = new ArrayList<>();
        try {
            //Comprobar si existe en el almacenamiento interno
            FileInputStream fis = context.openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            try {
                while (bufferedReader.ready()) {
                    receiveString.add(bufferedReader.readLine());
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
                    receiveString.add(bufferedReader.readLine());
                }

                inputStreamReader.close();

                return receiveString;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return null;
    }

    private void waitSurfaceConfiguration() {
        while (!this.initialConfigurationDone) ;
        if (this.configThread != null) {
            while (true) {
                try {
                    this.configThread.join();
                    this.configThread = null;
                    break;
                } catch (InterruptedException e) {
                    System.err.println("Config join error");
                    e.printStackTrace();
                }
            }
        }
    }

    // thread that's used only once, at engine creation, so
    // we can do the start configuration when engine is created,
    // and the run method waits till this is done
    private class SurfaceAvailable implements Runnable {

        EngineAndroid engine;

        SurfaceAvailable(EngineAndroid engine) {
            this.engine = engine;
        }

        @Override
        public void run() {
            myRenderManager.holderWait();
            mySceneManager.currentScene().init(this.engine);

            initialConfigurationDone = true;
        }
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
                    iA = new InputAndroid(input_x, input_y, InputType.TOUCH_DOWN,
                            motionEvent.getActionIndex());
                    myInputManager.addInput(iA);
                    handler.postDelayed(mLongPressed, 500);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacks(mLongPressed);
                    if (!goneFlag) {
                        iA = new InputAndroid(input_x, input_y, InputType.TOUCH_UP,
                                motionEvent.getActionIndex());
                        myInputManager.addInput(iA);
                        return true;
                    } else {
                        iA = new InputAndroid(input_x, input_y, InputType.TOUCH_LONG,
                                motionEvent.getActionIndex());
                        myInputManager.addInput(iA);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
//              if (!(Math.abs(input_x - input_x_original) <= 5 && Math.abs(input_y - input_y_original) <= 5))
                    iA = new InputAndroid(input_x, input_y, InputType.TOUCH_MOVE,
                            motionEvent.getActionIndex());
                    myInputManager.addInput(iA);
                    break;
            }
            return true;
        }
    }
}
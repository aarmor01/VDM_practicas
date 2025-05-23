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

import android.app.GameManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private LightSensor myLightSensor;

    // asset manager
    private final AssetManager assetManager;
    private final Context context;

    // current orientation
    private Orientation orientation;

    // thread variables
    private Thread renderThread;
    private boolean running;
    private boolean initConfig;

    public EngineAndroid(SurfaceView surface, AppCompatActivity activity, int w, int h, int bgColor) {
        // context
        this.context = activity.getBaseContext();
        this.assetManager = this.context.getAssets();

        // orientation (-1 because it starts PORTRAIT == 1, LANDSCAPE == 2)
        this.orientation = Orientation.values()[activity.getResources().getConfiguration().orientation - 1];

        // engine modules initialization
        this.myRenderManager = new RenderAndroid(surface, this.assetManager, w, h, bgColor);
        this.myAudioManager = new AudioAndroid(this.assetManager);
        this.mySceneManager = new SceneManager();
        this.myInputManager = new InputManager();
        this.myAdSystem = new AdSystemAndroid(activity, this.context);
        this.myIntentSystem = new IntentSystemAndroid(this.context);

        this.initConfig = false;

        // add input listener to window
        surface.setOnTouchListener(new InputListener());
        // add layout change listener to window
        surface.addOnLayoutChangeListener( new MyOnLayoutChangeListener());
    }

    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        // we wait for the initial configuration to end before starting the game cycle
        this.myRenderManager.isRenderReady(this.orientation);
        if(!this.initConfig){
            this.mySceneManager.currentScene().init(this);
            this.initConfig = true;
        }

        // resume audio
        this.myAudioManager.playMusic();

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
                this.mySceneManager.currentScene().update(deltaTime / 1000.0f, this);

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

        // pause audio
        this.myAudioManager.pauseMusic();
    }

    public void resume() {
        if (!this.running) {
            // resume engine
            this.running = true;

            this.renderThread = new Thread(this);
            this.renderThread.start();

            this.myLightSensor.onResume();
        }
    }

    public void pause() {
        if (this.running) {
            this.myLightSensor.onPause();

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

    public void setLightSensor(LightSensor lS) { this.myLightSensor = lS; }

    public LightSensor getLightSensor() { return this.myLightSensor; }

    public Context getContext() {return this.context;}

    // TODO: change scene init structure so we don't need this and we just return the enum in the method
    public Orientation getOrientation() {
        return orientation;
    }

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
            System.out.println("Unable to open input file.");
        }

        return file;
    }

    public FileOutputStream openOutputFile(String path) {
        FileOutputStream file = null;

        try {
            file = this.context.openFileOutput(path, Context.MODE_PRIVATE);
        } catch (Exception e) {
            System.out.println("Unable to open output file.");
        }

        return file;
    }

    public ArrayList<String> readText(String path, String file) {
        //Carga de archivo
        ArrayList<String> receiveString = new ArrayList<>();
        try {
            //Comprobar si existe en el almacenamiento interno
            FileInputStream fis = this.context.openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            try {
                while (bufferedReader.ready())
                    receiveString.add(bufferedReader.readLine());
                inputStreamReader.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return receiveString;

        } catch (FileNotFoundException e) {
            // Si no existe, crea un nuevo archivo en almacenamiento interno como copia desde assets
            System.out.println("Unable to read file.");
            // e.printStackTrace();
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(this.assetManager.open(path + file));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while (bufferedReader.ready())
                    receiveString.add(bufferedReader.readLine());

                inputStreamReader.close();

                return receiveString;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return null;
    }

    public String getChecksum(FileInputStream fis) throws NoSuchAlgorithmException, IOException {
        // Use MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        // Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        // Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1)
            md5Digest.update(byteArray, 0, bytesCount);

        // Get the hash's bytes
        byte[] bytes = md5Digest.digest();

        // This bytes[] has bytes in decimal format;
        // Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
            sb.append(String.format("%02x", bytes[i]));

        // return complete hash
        return sb.toString();
    }

    private class MyOnLayoutChangeListener implements View.OnLayoutChangeListener
    {
        public void onLayoutChange( View v, int left, int top, int right, int bottom,
            int leftWas, int topWas, int rightWas, int bottomWas )
        {
            myRenderManager.updateScale(orientation != Orientation.PORTRAIT);
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
            int input_y = (int) ((motionEvent.getY() - myRenderManager.getPosCanvasY()) / myRenderManager.getScale());
            int input_x = (int) ((motionEvent.getX() - myRenderManager.getPosCanvasX()) / myRenderManager.getScale());

            InputAndroid iA;

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
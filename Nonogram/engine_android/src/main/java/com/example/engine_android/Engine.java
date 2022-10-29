package com.example.engine_android;

import android.content.res.AssetManager;
import android.view.SurfaceView;

import com.example.engine_common.*;

public class Engine implements IEngine, Runnable {
    private Thread renderThread;
    private boolean running;
    private Render render;
    private IScene currentScene;
    private SurfaceView renderView;
    private  AssetManager assetManager;

    public void setScene(IScene s) {
        currentScene = s;
        currentScene.init(this);
    }
    @Override
    public IRender getRender() {
        return render;
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

    public void init(SurfaceView s, AssetManager aM) {
        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = s;
        assetManager = aM;
        this.render = new Render();
        this.render.init(this.renderView, assetManager);
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && render.getWindowWidth() == 0);
        // Espera activa. Sería más elegante al menos dormir un poco.

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        // Bucle de juego principal.
        while(running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            currentScene.update(elapsedTime);
            render.render(currentScene);
//            if (currentTime - informePrevio > 1000000000l) {
//                long fps = frames * 1000000000l / (currentTime - informePrevio);
//                System.out.println("" + fps + " fps");
//                frames = 0;
//                informePrevio = currentTime;
//            }
            ++frames;

                /*
                // Posibilidad: cedemos algo de tiempo. Es una medida conflictiva...
                try { Thread.sleep(1); } catch(Exception e) {}
    			*/
        }
    }

    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
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
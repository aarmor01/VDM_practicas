package com.example.app_android;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
class MyRenderClass implements Runnable{

    private SurfaceView myView;
    private SurfaceHolder holder;
    private Canvas canvas;
    private AssetManager aMan;

    private Thread renderThread;

    private boolean running;

    private Paint paint;

    private MyScene scene;

    public MyRenderClass(SurfaceView myView){
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFFFFFFFF);
    }

    public void setAssetManager(AssetManager a) {
        aMan = a;
    }

    public int getWidth(){
        return this.myView.getWidth();
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
        while(this.running && this.myView.getWidth() == 0);
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
            this.update(elapsedTime);
//            if (currentTime - informePrevio > 1000000000l) {
//                long fps = frames * 1000000000l / (currentTime - informePrevio);
//                System.out.println("" + fps + " fps");
//                frames = 0;
//                informePrevio = currentTime;
//            }
            ++frames;

            // Pintamos el frame
            while (!this.holder.getSurface().isValid());
            this.canvas = this.holder.lockCanvas();
            this.render();
            this.holder.unlockCanvasAndPost(canvas);

                /*
                // Posibilidad: cedemos algo de tiempo. Es una medida conflictiva...
                try { Thread.sleep(1); } catch(Exception e) {}
    			*/
        }
    }

    protected void update(double deltaTime) {
        scene.update(deltaTime);
    }

    public void setScene(MyScene scene) {
        this.scene = scene;
    }

    protected void renderCircle(float x, float y, float r){
        canvas.drawCircle(x, y, r, this.paint);
    }

    protected void renderFuente(String name, int size){
        try {
            String[] a = aMan.list("");
            for(int i=0; i<a.length; i++)
                System.out.println(a[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Typeface typeface = Typeface.createFromAsset(aMan, name);
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(size);
        canvas.drawText("Hola", 50, 200, paint);
    }

    protected void render() {
        // "Borramos" el fondo.
        this.canvas.drawColor(0xFF0000FF); // ARGB
        scene.render();
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
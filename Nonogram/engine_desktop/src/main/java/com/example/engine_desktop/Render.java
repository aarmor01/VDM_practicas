package com.example.engine_desktop;

import com.example.engine_interfaces.IFont;
import com.example.engine_interfaces.IImage;
import com.example.engine_interfaces.IRender;
import com.example.engine_interfaces.IScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Render implements IRender {

    private JFrame myWin;
    private BufferStrategy myBufferStrategy;
    private Graphics2D myGraphics2D;

    public void init(JFrame win) {
        this.myWin = win;
        this.myBufferStrategy = this.myWin.getBufferStrategy();
        this.myGraphics2D = (Graphics2D) myBufferStrategy.getDrawGraphics();

        // what does it do when the window gets resized
        this.myWin.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                //Component c = (Component)evt.getSource();
                System.out.println("componentResized: " + evt.getSource());
                myGraphics2D.dispose();

                myBufferStrategy.show();
                myGraphics2D = (Graphics2D) myBufferStrategy.getDrawGraphics();
            }
        });
    }

    public void render(IScene currScene) {
        // Pintamos el frame
        do {
            do {
                Graphics graphics = this.myBufferStrategy.getDrawGraphics();
                try {
                    currScene.render(this);
                } finally {
                    graphics.dispose(); //Elimina el contexto gráfico y libera recursos del sistema realacionado
                }
            } while (this.myBufferStrategy.contentsRestored());
            this.myBufferStrategy.show();
        } while (this.myBufferStrategy.contentsLost());
    }

    @Override
    public IImage newImage() {
        return null;
    }

    @Override
    public IFont newFont() {
        return null;
    }

    @Override
    public void setResolution() {

    }

    @Override
    public void setColor() {

    }

    @Override
    public void setFont() {

    }

    @Override
    public void drawImage() {

    }

    @Override
    public void drawRectangle() {

    }

    @Override
    public void fillRectangle() {

    }

    @Override
    public void drawLine() {

    }

    @Override
    public void drawText() {

    }

    @Override
    public int getWindowWidth() {
        return this.myWin.getWidth();
    }

    @Override
    public int getWindowHeight() {
        return this.myWin.getHeight();
    }
}


////Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
//public class MyRenderClass implements Runnable{
//
//    private JFrame myView;
//    private BufferStrategy bufferStrategy;
//    private Graphics2D graphics2D;
//
//    private Thread renderThread;
//
//    private boolean running;
//
//    private MyScene scene;
//
//
//    protected void update(double deltaTime) {
//        this.scene.update(deltaTime);
//    }
//
//    public void setScene(MyScene scene) {
//        this.scene = scene;
//    }
//
//    protected void renderCircle(float x, float y, float r){
//        this.graphics2D.setColor(Color.white);
//        this.graphics2D.fillOval((int)x, (int)y, (int)r*2, (int)r*2);
//        this.graphics2D.setPaintMode();
//    }
//
//    protected void renderText() {
//
//    }
//
//    protected void render() {
//        // "Borramos" el fondo.
//        this.graphics2D.setColor(Color.BLUE);
//        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
//        // Pintamos la escena
//        this.scene.render();
//    }
//
//    public void resume() {
//        if (!this.running) {
//            // Solo hacemos algo si no nos estábamos ejecutando ya
//            // (programación defensiva)
//            this.running = true;
//            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
//            this.renderThread = new Thread(this);
//            this.renderThread.start();
//        }
//    }
//
//    public void pause() {
//        if (this.running) {
//            this.running = false;
//            while (true) {
//                try {
//                    this.renderThread.join();
//                    this.renderThread = null;
//                    break;
//                } catch (InterruptedException ie) {
//                    // Esto no debería ocurrir nunca...
//                }
//            }
//        }
//    }
//}
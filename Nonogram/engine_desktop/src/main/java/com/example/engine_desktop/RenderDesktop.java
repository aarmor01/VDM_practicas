package com.example.engine_desktop;

import com.example.engine_common.IFont;
import com.example.engine_common.IImage;
import com.example.engine_common.IRender;
import com.example.engine_common.IScene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class RenderDesktop implements IRender {

    private JFrame myWin;
    private BufferStrategy myBufferStrategy;
    private Graphics2D myGraphics2D;

    private int baseWidth;
    private int baseHeight;

    private HashMap<String, IFont> fonts;
    private HashMap<String, IImage> images;

    public void init(JFrame win) {
        this.myWin = win;
        this.myBufferStrategy = this.myWin.getBufferStrategy();
        this.myGraphics2D = (Graphics2D)myBufferStrategy.getDrawGraphics();

        // what does it do when the window gets resized
        this.myWin.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                //Component c = (Component)evt.getSource();
                System.out.println("componentResized: " + evt.getSource());
                myGraphics2D.dispose();

                myBufferStrategy.show();
                myGraphics2D = (Graphics2D)myBufferStrategy.getDrawGraphics();
            }
        });

        baseWidth = this.myWin.getWidth();
        baseHeight = this.myWin.getHeight();

        fonts = new HashMap<>();
        images = new HashMap<>();
    }

    public void prepareFrame() {
        this.clear();
    }

    public void finishFrame() {
        this.myBufferStrategy.getDrawGraphics().dispose();
    }

    public boolean swapBuffer() {
        if (this.myBufferStrategy.contentsRestored())
            return false;

        this.myBufferStrategy.show();

        return this.myBufferStrategy.contentsLost();
    }

    @Override
    public String loadImage(String filePath) {
        File imageFile = new File(filePath);
        images.put(imageFile.getName(), new ImageDesktop(filePath));
        return imageFile.getName();
    }

    @Override
    public String loadFont(String filePath) {

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
    public void drawCircle(int x, int y, int r) {
        this.myGraphics2D.setColor(Color.white);
        this.myGraphics2D.fillOval((int) x, (int) y, (int) r * 2, (int) r * 2);
        this.myGraphics2D.setPaintMode();
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

    protected void clear() {
        // "Borramos" el fondo.
        this.myGraphics2D.setColor(Color.WHITE);
        this.myGraphics2D.fillRect(0, 0, this.getWindowWidth(), this.getWindowHeight());
    }
}
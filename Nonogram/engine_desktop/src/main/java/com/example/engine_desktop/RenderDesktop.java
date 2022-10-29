package com.example.engine_desktop;

import com.example.engine_common.interfaces.IFont;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.shared.FontType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JFrame;

public class RenderDesktop implements IRender {

    private JFrame myWin;
    private BufferStrategy myBufferStrategy;
    private Graphics2D myGraphics2D;

    private Insets borders;

    private int baseWidth;
    private int baseHeight;

    private HashMap<String, IFont> fonts;
    private HashMap<String, ImageDesktop> images;

    public void init(JFrame win) {
        // obtain window and render data
        this.myWin = win;
        this.myBufferStrategy = this.myWin.getBufferStrategy();
        this.myGraphics2D = (Graphics2D)myBufferStrategy.getDrawGraphics();

        // adjust to JFrame borders
        this.borders = this.myWin.getInsets();
        this.myWin.setSize(this.myWin.getWidth() + this.borders.left + this.borders.right,
                this.myWin.getHeight() + this.borders.top + this.borders.bottom);
        this.myGraphics2D.translate(this.borders.top, this.borders.left);

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
        images.put(imageFile.getName(), new ImageDesktop(imageFile));
        return imageFile.getName();
    }

    @Override
    public String loadFont(String filePath, FontType type, int size) {
        File fontFile = new File(filePath);
        String id = fontFile.getName() + type.toString() + size;
        fonts.put(id, new FontDesktop(fontFile, type, size));
        return id;
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
    public void drawCircle(int x, int y, int r) {
        this.myGraphics2D.setColor(Color.white);
        this.myGraphics2D.fillOval((int) x, (int) y, (int) r * 2, (int) r * 2);
        this.myGraphics2D.setPaintMode();
    }

    @Override
    public int getWidth() {
        return this.baseWidth;
    }

    @Override
    public int getHeight() {
        return this.baseHeight;
    }

    protected void clear() {
        // "Borramos" el fondo.
        this.myGraphics2D.setColor(Color.WHITE);
        this.myGraphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
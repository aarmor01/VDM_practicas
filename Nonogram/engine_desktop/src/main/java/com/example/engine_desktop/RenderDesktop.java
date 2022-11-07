package com.example.engine_desktop;

import com.example.engine_common.interfaces.IFont;
import com.example.engine_common.interfaces.IImage;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.shared.FontType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
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
    private double baseDPI;

    private double scaleFactor;

    private HashMap<String, FontDesktop> fonts;
    private HashMap<String, ImageDesktop> images;

    public RenderDesktop(JFrame win) {
        // obtain window and render data
        this.myWin = win;
        this.myBufferStrategy = this.myWin.getBufferStrategy();
        this.myGraphics2D = (Graphics2D) myBufferStrategy.getDrawGraphics();

        // safe prev size
        baseWidth = this.myWin.getWidth();
        baseHeight = this.myWin.getHeight();
        baseDPI = this.myGraphics2D.getTransform().getScaleX();

        // adjust to JFrame borders
        this.borders = this.myWin.getInsets();
        this.myWin.setSize(this.myWin.getWidth() + this.borders.left + this.borders.right,
                this.myWin.getHeight() + this.borders.top + this.borders.bottom);
        this.myGraphics2D = (Graphics2D) myBufferStrategy.getDrawGraphics();

        // what does it do when the window gets resized discarded because stuttering
//        this.myWin.addComponentListener(new ComponentAdapter() {
//            public void componentResized(ComponentEvent evt) {
//
//            }
//        });

        // start resource managers
        fonts = new HashMap<>();
        images = new HashMap<>();
    }

    public void prepareFrame() {
        this.myGraphics2D = (Graphics2D) this.myBufferStrategy.getDrawGraphics();

        double scaleX = (myWin.getWidth() - borders.left - borders.right) / (float) baseWidth;
        double scaleY = (myWin.getHeight() - borders.top - borders.bottom) / (float) baseHeight;
        scaleFactor = Math.min(scaleX * baseDPI, scaleY * baseDPI);

        AffineTransform at = this.myGraphics2D.getTransform();
        System.out.println(this.borders.left * scaleFactor + " " + this.borders.top * scaleFactor);
        at.setToTranslation((this.myWin.getWidth() / 2 - baseWidth * (scaleFactor / baseDPI) / 2) * baseDPI,
                (((this.myWin.getHeight()) + this.borders.top - this.borders.bottom) / 2 - baseHeight * (scaleFactor / baseDPI) / 2) * baseDPI);
        this.myGraphics2D.setTransform(at);
        at.setToScale((scaleFactor / at.getScaleX()), (scaleFactor / at.getScaleY()));
        this.myGraphics2D.transform(at);

        // "Borramos" el fondo.
        this.myGraphics2D.setColor(Color.white);
        this.myGraphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
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
    public void setColor(int hexColor) {
        this.myGraphics2D.setColor(new Color(hexColor, true));
    }

    @Override
    public void setFont(String fontID) {
        this.myGraphics2D.setFont(fonts.get(fontID).getFont());
    }

    @Override
    public void drawImage(int x, int y, int width, int height, String imageID) {
        IImage image = images.get(imageID);
        this.myGraphics2D.drawImage(images.get(imageID).getImage(), x, y, width, height,
                0, 0, image.getWidth(), image.getHeight(), null);
        this.myGraphics2D.setPaintMode();
    }

    @Override
    public void drawText(int x, int y, String text){
        this.myGraphics2D.drawString(text, x, y);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill){
        if (!fill) this.myGraphics2D.drawRect(x, y, width, height);
        else      this.myGraphics2D.fillRect(x, y, width, height);
        this.myGraphics2D.setPaintMode();
    }

    @Override
    public void drawLine(int og_x, int og_y, int dst_x, int dst_y){
        this.myGraphics2D.drawLine(og_x, og_y, dst_x, dst_y);
        this.myGraphics2D.setPaintMode();
    }

    @Override
    public void drawCircle(int x, int y, int r) {
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

    public int getOffsetX() { return this.borders.left; }

    public int getOffsetY() { return this.borders.top; }

    public double getDPI() { return this.baseDPI; }
}
package com.example.engine_desktop;

import com.example.engine_common.interfaces.IImage;
import com.example.engine_common.interfaces.IRender;

import com.example.engine_common.shared.FontType;

import javax.swing.JFrame;

import java.awt.image.BufferStrategy;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Color;
import java.awt.geom.AffineTransform;

import java.io.File;

import java.util.HashMap;

public class RenderDesktop implements IRender {
    // render window variables
    private JFrame window;
    private BufferStrategy bufferStrategy;
    private Graphics2D canvas;

    // window borders
    private Insets borders;

    // logic sizes
    private int canvasWidth;
    private int canvasHeight;

    // scale values
    private double ogDPI;
    private double scaleFactor;

    // margin values
    private int marginWidth;
    private int marginHeight;

    // default background color
    private int bgColor;

    // render resources
    private HashMap<String, FontDesktop> fonts;
    private HashMap<String, ImageDesktop> images;

    public RenderDesktop(JFrame win) {
        // obtain window and render data
        this.window = win;
        this.bufferStrategy = this.window.getBufferStrategy();
        this.canvas = (Graphics2D) bufferStrategy.getDrawGraphics();

        // canvas distribution
        this.canvasWidth = this.window.getWidth();
        this.canvasHeight = this.window.getHeight();

        // safe configs
        this.ogDPI = this.canvas.getTransform().getScaleX();
        this.borders = this.window.getInsets();

        // adjust to JFrame insets
        this.window.setSize(this.window.getWidth() + this.borders.left + this.borders.right,
                this.window.getHeight() + this.borders.top + this.borders.bottom);
        this.canvas = (Graphics2D) bufferStrategy.getDrawGraphics();

        // bg color
        this.bgColor = 0xFF000000;

        // start resource managers
        fonts = new HashMap<>();
        images = new HashMap<>();
    }

    public void prepareFrame(int bgCanvasColor) {
        // obtain next buffer
        this.canvas = (Graphics2D)this.bufferStrategy.getDrawGraphics();

        // adapt canvas to scale
        scaleCanvas();

        // clear
        setColor(bgCanvasColor);
        drawRectangle(0, 0, this.getWidth(), this.getHeight(), true);
    }

    public void finishFrame() {
        // draw margins above everything else
        drawMargins();

        // flag buffer as available
        this.bufferStrategy.getDrawGraphics().dispose();
    }

    public boolean swapBuffer() {
        if (this.bufferStrategy.contentsRestored())
            return false;

        this.bufferStrategy.show();

        return this.bufferStrategy.contentsLost();
    }

    @Override
    public String loadImage(String filePath) {
        File imageFile = new File(filePath);
        if(!images.containsKey(imageFile.getName()))
            images.put(imageFile.getName(), new ImageDesktop(imageFile));
        return imageFile.getName();
    }

    @Override
    public String loadFont(String filePath, FontType type, int size) {
        File fontFile = new File(filePath);
        String fontID = fontFile.getName() + type.toString() + size;
        if(!fonts.containsKey(fontID))
            fonts.put(fontID, new FontDesktop(fontFile, type, size));
        return fontID;
    }

    @Override
    public void setColor(int hexColor) {
        this.canvas.setColor(new Color(hexColor, true));
    }

    @Override
    public void setFont(String fontID) {
        this.canvas.setFont(fonts.get(fontID).getFont());
    }

    @Override
    public void drawLine(int og_x, int og_y, int dst_x, int dst_y) {
        this.canvas.drawLine(og_x, og_y, dst_x, dst_y);
        this.canvas.setPaintMode();
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill) {
        if (fill) this.canvas.fillRect(x, y, width, height);
        else      this.canvas.drawRect(x, y, width, height);
        this.canvas.setPaintMode();
    }

    @Override
    public void drawCircle(int x, int y, int r, boolean fill) {
        if (fill) this.canvas.fillOval(x, y, r * 2, r * 2);
        else      this.canvas.drawOval(x, y, r * 2, r * 2);
        this.canvas.setPaintMode();
    }

    @Override
    public void drawImage(int x, int y, int width, int height, String imageID) {
        IImage image = images.get(imageID);
        this.canvas.drawImage(images.get(imageID).getImage(), x, y, width, height,
                0, 0, image.getWidth(), image.getHeight(), null);
        this.canvas.setPaintMode();
    }

    @Override
    public void drawText(int x, int y, String text) {
        this.canvas.drawString(text, x, y);
    }

    private void scaleCanvas() {
        // select the lower scale to make proportion viable
        double scaleX = (window.getWidth() - borders.left - borders.right) / (float) canvasWidth;
        double scaleY = (window.getHeight() - borders.top - borders.bottom) / (float) canvasHeight;
        scaleFactor = Math.min(scaleX * ogDPI, scaleY * ogDPI);

        int tx = (int)((this.window.getWidth() / 2 - canvasWidth * (scaleFactor / ogDPI) / 2) * ogDPI);
        int ty = (int)((((this.window.getHeight()) + this.borders.top - this.borders.bottom) / 2 -
                canvasHeight * (scaleFactor / ogDPI) / 2) * ogDPI);

        marginWidth = Math.max(tx - this.borders.left, 0);
        marginHeight = Math.max(ty - this.borders.top, 0);

        AffineTransform at = this.canvas.getTransform();
        at.setToTranslation(tx, ty);
        this.canvas.setTransform(at);
        at.setToScale((scaleFactor / at.getScaleX()), (scaleFactor / at.getScaleY()));
        this.canvas.transform(at);
    }

    private void drawMargins() {
        setColor(this.bgColor);

        drawRectangle((int)(-marginWidth / scaleFactor), 0, (int)(marginWidth / scaleFactor), canvasHeight, true);
        drawRectangle(canvasWidth, 0, (int)(marginWidth / scaleFactor), canvasHeight, true);

        drawRectangle(0, (int)(-marginHeight / scaleFactor), canvasWidth, (int)(marginHeight / scaleFactor), true);
        drawRectangle(0, canvasHeight, canvasWidth, (int)(marginHeight / scaleFactor), true);
    }

    public boolean windowCreated() { return this.window.getWidth() != 0; }

    @Override
    public int getWidth() {
        return this.canvasWidth;
    }

    @Override
    public int getHeight() {
        return this.canvasHeight;
    }

    public int getOffsetX() { return (int)Math.round((this.borders.left  + marginWidth)/ ogDPI); }

    public int getOffsetY() { return (int)Math.round((this.borders.top + marginHeight)/ ogDPI) ; }

    public double getScale() { return this.scaleFactor / this.ogDPI; }
}
package com.example.engine_desktop;

import com.example.engine_common.interfaces.IImage;
import com.example.engine_common.interfaces.IRender;

import com.example.engine_common.shared.FontType;

import javax.swing.JFrame;

import java.awt.Font;
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

    public RenderDesktop(JFrame win, int bg) {
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
        this.bgColor = bg;
        this.window.setBackground(new Color(bgColor));

        // start resource managers
        this.fonts = new HashMap<>();
        this.images = new HashMap<>();
    }

    public void prepareFrame(int bgCanvasColor) {
        // obtain next buffer
        this.canvas = (Graphics2D)this.bufferStrategy.getDrawGraphics();

        // adapt canvas to scale
        this.scaleCanvas();

        // clear
        this.setColor(bgCanvasColor);
        this.drawRectangle(0, 0, this.getWidth(), this.getHeight(), true);
    }

    public void finishFrame() {
        // draw margins above everything else
        this.drawMargins();

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
        if(!this.images.containsKey(imageFile.getName()))
            this.images.put(imageFile.getName(), new ImageDesktop(imageFile));
        return imageFile.getName();
    }

    @Override
    public String loadFont(String filePath, FontType type, int size) {
        File fontFile = new File(filePath);
        String fontID = fontFile.getName() + type.toString() + size;
        if(!this.fonts.containsKey(fontID))
            this.fonts.put(fontID, new FontDesktop(fontFile, type, size));
        return fontID;
    }

    @Override
    public void setColor(int hexColor) {
        this.canvas.setColor(new Color(hexColor, true));
    }

    @Override
    public void setFont(String fontID) {
        this.canvas.setFont(this.fonts.get(fontID).getFont());
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
        ImageDesktop image = this.images.get(imageID);
        this.canvas.drawImage(image.getImage(), x, y, x + width, y + height,
                0, 0, image.getWidth(), image.getHeight(), null);
        this.canvas.setPaintMode();
    }

    @Override
    public void drawText(int x, int y, String text) {
        this.canvas.drawString(text, x, y);
    }

    @Override
    public int getTextWidth(String fontID, String text) {
        Font currFont = this.fonts.get(fontID).getFont();
        return this.canvas.getFontMetrics(currFont).stringWidth(text);
    }

    @Override
    public int getTextHeight(String fontID) { return this.fonts.get(fontID).getFont().getSize(); }

    @Override
    public int getWidth() { return this.canvasWidth; }

    @Override
    public int getHeight() { return this.canvasHeight; }

    public boolean windowCreated() { return this.window.getWidth() != 0; }

    public int getOffsetX() { return (int)Math.round((this.borders.left + this.marginWidth) / this.ogDPI); }

    public int getOffsetY() { return (int)Math.round((this.borders.top + this.marginHeight) / this.ogDPI); }

    public double getScale() { return this.scaleFactor / this.ogDPI; }

    private void scaleCanvas() {
        // select the lower scale to make proportion viable
        double scaleX = (window.getWidth() - this.borders.left - this.borders.right) / (float) this.canvasWidth;
        double scaleY = (window.getHeight() - this.borders.top - this.borders.bottom) / (float) this.canvasHeight;
        this.scaleFactor = Math.min(scaleX * this.ogDPI, scaleY * this.ogDPI);

        // we translate with the scale in mind since when you scale below, it does with upper left
        // portion as the center, so we take the logic width/height scale into account so it truly fits
        // in the center
        int tx = (int)((this.window.getWidth() / 2 - this.canvasWidth * (this.scaleFactor / this.ogDPI) / 2) * this.ogDPI);
        int ty = (int)((((this.window.getHeight()) + this.borders.top - this.borders.bottom) / 2 -
                this.canvasHeight * (this.scaleFactor / this.ogDPI) / 2) * this.ogDPI);

        // we also get the margins width and height data to print them afterwards
        this.marginWidth = Math.max(tx - this.borders.left, 0);
        this.marginHeight = Math.max(ty - this.borders.top, 0);

        // set translation and scale to the transformation matrix
        AffineTransform at = this.canvas.getTransform();
        at.setToTranslation(tx, ty);
        this.canvas.setTransform(at);
        at.setToScale((this.scaleFactor / at.getScaleX()), (this.scaleFactor / at.getScaleY()));
        this.canvas.transform(at);
    }

    private void drawMargins() {
        this.setColor(this.bgColor);

        this.drawRectangle((int)(-this.marginWidth / this.scaleFactor), 0,
                (int)(this.marginWidth / this.scaleFactor), this.canvasHeight, true);
        this.drawRectangle(this.canvasWidth, 0, (int)(this.marginWidth / this.scaleFactor), this.canvasHeight, true);

        this.drawRectangle(0, (int)(-this.marginHeight / this.scaleFactor),
                this.canvasWidth, (int)(this.marginHeight / this.scaleFactor), true);
        this.drawRectangle(0, this.canvasHeight, this.canvasWidth, (int)(this.marginHeight / this.scaleFactor), true);
    }
}
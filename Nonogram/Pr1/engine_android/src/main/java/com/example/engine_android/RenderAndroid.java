package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.shared.FontType;

import java.io.File;
import java.util.HashMap;

public class RenderAndroid implements IRender {
    //android graphic variables
    private SurfaceView myView;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;

    //resource managers
    private HashMap<String, ImageAndroid> images;
    private HashMap<String, FontAndroid> fonts;
    private AssetManager assetManager;

    //canvas position info
    private int posCanvasX, posCanvasY;

    //canvas scale, height and width
    private int baseWidth;
    private int baseHeight;
    private float scale;

    //background color
    private int bgColor;

    public RenderAndroid(SurfaceView myView, AssetManager aM, int width, int height, int bgColor) {
        // canvas
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();

        this.assetManager = aM;

        // resources
        this.fonts = new HashMap<>();
        this.images = new HashMap<>();

        // initializes canvas values
//        this.scale = ratio;
        this.baseWidth = width;
        this.baseHeight = height;

        // sets the background color
        this.bgColor = bgColor;

        // add layout change listener
        this.myView.addOnLayoutChangeListener(new MyOnLayoutChangeListener());
    }

    public void updateScale() {
        int hWidth = this.holder.getSurfaceFrame().width();
        int hHeight = this.holder.getSurfaceFrame().height();
        this.scale = hWidth / (float) (this.baseWidth);
        this.posCanvasX = (int) ((hWidth - this.baseWidth * this.scale) / 2);
        this.posCanvasY = (int) ((hHeight - this.baseHeight * this.scale) / 2);
    }

    public boolean surfaceValid() {
        return this.holder.getSurface().isValid();
    }

    public void clear() {
        this.canvas = this.holder.lockCanvas();
        this.canvas.drawColor(this.bgColor);
        this.canvas.translate(this.posCanvasX, this.posCanvasY);
        this.canvas.scale(this.scale, this.scale);
        setColor(this.bgColor);
        drawRectangle(0, 0, this.baseWidth, this.baseHeight, true);
    }

    public void present() {
        this.holder.unlockCanvasAndPost(this.canvas);
    }

    @Override
    public String loadImage(String filePath) {
        File imageFile = new File(filePath);
        if (!this.images.containsKey(imageFile.getName()))
            this.images.put(imageFile.getName(), new ImageAndroid(filePath, this.assetManager));
        return imageFile.getName();
    }

    @Override
    public String loadFont(String filePath, FontType type, int size) {
        File fontFile = new File(filePath);
        String fontID = fontFile.getName() + type.toString() + size;
        if (!this.fonts.containsKey(fontID))
            this.fonts.put(fontID, new FontAndroid(filePath, this.assetManager, size, type));
        return fontID;
    }

    @Override
    public void setColor(int hexColor) {
        this.paint.setColor(hexColor);
    }

    @Override
    public void setFont(String fontID) {
        FontAndroid font = this.fonts.get(fontID);
        this.paint.setTypeface(font.getFont());
        this.paint.setTextSize(font.getSize());
    }

    @Override
    public void drawLine(int og_x, int og_y, int dst_x, int dst_y) {
        this.canvas.drawLine(og_x, og_y, dst_x, dst_y, this.paint);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill) {
        this.paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        this.canvas.drawRect(x, y, x + width, y + height, this.paint);
    }

    @Override
    public void drawCircle(int x, int y, int r, boolean fill) {
        this.paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        this.canvas.drawCircle(x, y, r, this.paint);
    }

    @Override
    public void drawImage(int x, int y, int width, int height, String imageID) {
        Bitmap image = this.images.get(imageID).getImage();
        Rect src = new Rect(0, 0, image.getWidth(), image.getHeight());
        Rect dst = new Rect(x, y, x + width, y + height);
        this.canvas.drawBitmap(image, src, dst, this.paint);
    }

    @Override
    public void drawText(int x, int y, String text) {
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.canvas.drawText(text, x, y, this.paint);
    }

    @Override
    public int getTextWidth(String fontID, String text) {
        Typeface prev_font = this.paint.getTypeface();
        FontAndroid font = this.fonts.get(fontID);

        this.paint.setTypeface(font.getFont());
        float width = this.paint.measureText(text);
        this.paint.setTypeface(prev_font);

        return (int) width;
    }

    @Override
    public int getTextHeight(String fontID) {
        return this.fonts.get(fontID).getSize();
    }

    @Override
    public int getWidth() {
        return this.myView.getWidth();
    }

    @Override
    public int getHeight() {
        return this.myView.getHeight();
    }

    public boolean isRenderReady() {
        while (this.holder.getSurfaceFrame().width() == 0) ;
        updateScale();
        return true;
    }

    public int getPosCanvasX() {
        return this.posCanvasX;
    }

    public int getPosCanvasY() {
        return this.posCanvasY;
    }

    public float getScale() {
        return this.scale;
    }

    private class MyOnLayoutChangeListener implements View.OnLayoutChangeListener {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int leftWas, int topWas, int rightWas, int bottomWas) {
            updateScale();
        }
    }
}
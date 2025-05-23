package com.example.engine_android.Modules;

import com.example.engine_android.DataStructures.FontAndroid;
import com.example.engine_android.DataStructures.ImageAndroid;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.util.HashMap;

public class RenderAndroid {
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
    private boolean landscape;

    //background color
    private int bgColor;

    public RenderAndroid(SurfaceView myView, AssetManager aM, int w, int h, int bgColor) {
        this.myView = myView;

        this.holder = this.myView.getHolder();
        this.paint = new Paint();

        this.assetManager = aM;

        // resources
        this.fonts = new HashMap<>();
        this.images = new HashMap<>();

        // initializes canvas values
        this.baseWidth = w;
        this.baseHeight = h;
        this.landscape = false;

        // sets the background color
        this.bgColor = bgColor;
    }

    public void updateScale (boolean landscape) {
        System.out.println(this.holder.getSurfaceFrame().width() + " " + this.holder.getSurfaceFrame().height());
        System.out.println(landscape + " " + posCanvasX + "  " + posCanvasY + "  " + scale);
        System.out.println("");
        this.scale = (landscape ? this.holder.getSurfaceFrame().height() :
                this.holder.getSurfaceFrame().width()) / (float) (this.baseWidth);
        this.posCanvasX = (int) ((this.holder.getSurfaceFrame().width()
                - ((landscape ? this.baseHeight : this.baseWidth) * this.scale)) / 2);
        this.posCanvasY = (int) ((this.holder.getSurfaceFrame().height()
                - ((landscape ? this.baseWidth : this.baseHeight) * this.scale)) / 2);
        System.out.println(this.holder.getSurfaceFrame().width() + " " + this.holder.getSurfaceFrame().height());
        System.out.println(landscape + " " + posCanvasX + "  " + posCanvasY + "  " + scale);
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

    public String loadImage(String filePath) {
        File imageFile = new File(filePath);
        if (!this.images.containsKey(imageFile.getName()))
            this.images.put(imageFile.getName(), new ImageAndroid(filePath, this.assetManager));
        return imageFile.getName();
    }

    public String loadFont(String filePath, FontType type, int size) {
        File fontFile = new File(filePath);
        String fontID = fontFile.getName() + type.toString() + size;
        if (!this.fonts.containsKey(fontID))
            this.fonts.put(fontID, new FontAndroid(filePath, this.assetManager, size, type));
        return fontID;
    }

    public void setColor(int hexColor) {
        this.paint.setColor(hexColor);
    }

    public void setFont(String fontID) {
        FontAndroid font = this.fonts.get(fontID);
        this.paint.setTypeface(font.getFont());
        this.paint.setTextSize(font.getSize());
    }

    public void setBackGroundColor(int hexColor) {
        bgColor = hexColor;
    }

    public void drawLine(int og_x, int og_y, int dst_x, int dst_y) {
        this.canvas.drawLine(og_x, og_y, dst_x, dst_y, this.paint);
    }

    public void drawRectangle(int x, int y, int width, int height, boolean fill) {
        this.paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        this.canvas.drawRect(x, y, x + width, y + height, this.paint);
    }

    public void drawCircle(int x, int y, int r, boolean fill) {
        this.paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        this.canvas.drawCircle(x, y, r, this.paint);
    }

    public void drawImage(int x, int y, int width, int height, String imageID) {
        Bitmap image = this.images.get(imageID).getImage();
        Rect src = new Rect(0, 0, image.getWidth(), image.getHeight());
        Rect dst = new Rect(x, y, x + width, y + height);
        this.canvas.drawBitmap(image, src, dst, this.paint);
    }

    public void drawText(int x, int y, String text) {
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        String[] txSpl = text.split("\n");
        for (String l : txSpl) {
            this.canvas.drawText(l, x, y, this.paint);
            y += this.paint.getTextSize();
        }
    }

    public int getTextWidth(String fontID, String text) {
        Typeface prev_font = this.paint.getTypeface();
        FontAndroid font = this.fonts.get(fontID);

        String[] txSpl = text.split("\n");
        this.paint.setTypeface(font.getFont());

        float width = 0;
        for (String l : txSpl)
            width = Math.max(paint.measureText(l), width);

        this.paint.setTypeface(prev_font);

        return (int) width;
    }

    public int getTextHeight(String fontID) {
        return this.fonts.get(fontID).getSize();
    }

    public int getWidth() {
        return this.myView.getWidth();
    }

    public int getHeight() {
        return this.myView.getHeight();
    }

    public void isRenderReady(EngineAndroid.Orientation ori) {
        while (this.holder.getSurfaceFrame().width() == 0);
        updateScale(ori != EngineAndroid.Orientation.PORTRAIT);
    }

    public int getPosCanvasX() {
        return posCanvasX;
    }

    public int getPosCanvasY() {
        return posCanvasY;
    }

    public float getScale() {
        return scale;
    }
}
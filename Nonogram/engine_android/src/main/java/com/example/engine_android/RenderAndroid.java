package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
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

    //screen orientation info
    private boolean verticalScreen;
    private boolean changedScreen;

    //canvas position info
    private int posCanvasX, posCanvasY;

    //canvas scale, height and width
    private int baseWidth;
    private int baseHeight;
    private float scale;


    public RenderAndroid(SurfaceView myView, AssetManager aM, float ratio) {
        //
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.assetManager = aM;
        this.paint = new Paint();
        this.fonts = new HashMap<>();
        this.images = new HashMap<>();
        this.verticalScreen = true;
        this.changedScreen = false;

        //initializes canvas values
        this.scale = ratio;
    }

    public void scaleApp() {
        while(holder.getSurfaceFrame().width() == 0);
        //400x600
        //x----y
        float w = holder.getSurfaceFrame().width();
        float y = holder.getSurfaceFrame().height();
        float scaleX = w;
        float scaleY = y;
        if (scaleX * scale < scaleY) scaleY = scaleX / scale;
        else scaleX = scaleY * scale;
        posCanvasX = (int) (w - scaleX) / 2;
        posCanvasY = (int) (y - scaleY) / 2;
        this.baseWidth = (int)scaleX;
        this.baseHeight = (int)scaleY;
    }

    public boolean surfaceValid() {
        return this.holder.getSurface().isValid();
    }

    public void clear() {
        this.canvas = this.holder.lockCanvas();
        canvas.drawColor(0xFFAAAAAA);
        canvas.translate(posCanvasX, posCanvasY);
        setColor(0xFFFFFFFF);
        drawRectangle(0, 0, baseWidth, baseHeight, true);
    }

    public void present() {
//        if (changedScreen) {;
//            double scaleX = myView.getWidth()  / (float)baseWidth;
//            double scaleY = myView.getHeight()  / (float)baseHeight;
//            double scaleFactor = Math.min(scaleX, scaleY);
//
//            if (verticalScreen)
//                canvas.rotate(-90);
//            else canvas.rotate(90);
//
//        }
        this.holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public String loadImage(String filePath) {
        File imageFile = new File(filePath);
        String convFilepath = filePath.replaceAll("./assets/", "");
        if(!images.containsKey(imageFile.getName()))
            images.put(imageFile.getName(), new ImageAndroid(convFilepath, assetManager));
        return imageFile.getName();
    }

    @Override
    public String loadFont(String filePath, FontType type, int size) {
        File fontFile = new File(filePath);
        String fontID = fontFile.getName() + type.toString() + size;
        String convFilepath = filePath.replaceAll("./assets/", "");
        if(!fonts.containsKey(fontID))
            fonts.put(fontID, new FontAndroid(convFilepath, assetManager, size, type));
        return fontID;
    }

    @Override
    public void setColor(int hexColor) {
        paint.setColor(hexColor);
    }

    @Override
    public void setFont(String fontID) {
        FontAndroid font = fonts.get(fontID);
        paint.setTypeface(font.getFont());
        paint.setTextSize(font.getSize());
    }

    @Override
    public void drawImage(int x, int y, int width, int height, String imageID) {
        Bitmap image = images.get(imageID).getImage();
        Rect src = new Rect(0,0,image.getWidth(), image.getHeight());
        Rect dst = new Rect(x, y, x + width, y+height);
        canvas.drawBitmap(image, src, dst, paint);
    }

    @Override
    public void drawText(int x, int y, String text) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public int getTextWidth(String fontID, String text) {
        Typeface prev_font = paint.getTypeface();
        FontAndroid font = fonts.get(fontID);

        paint.setTypeface(font.getFont());
        float width = paint.measureText(text);
        paint.setTypeface(prev_font);

        return (int)width;
    }

    @Override
    public int getTextHeight(String fontID) {
        return fonts.get(fontID).getSize();
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill) {
        paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    @Override
    public void drawLine(int og_x, int og_y, int dst_x, int dst_y) {
        canvas.drawLine(og_x, og_y, dst_x, dst_y, paint);
    }

    @Override
    public void drawCircle(int x, int y, int r, boolean fill) {
        paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        canvas.drawCircle(x, y, r, paint);
    }

    @Override
    public int getWidth() {
        return baseWidth;
    }

    public int getViewWidth() {
        return myView.getWidth();
    }

    public int getViewHeight() { return myView.getHeight(); }
    @Override
    public int getHeight() {
        return baseHeight;
    }

    public void changeScreen(boolean vertical) {
        if (verticalScreen == vertical)
            return;

        changedScreen = true;
        verticalScreen = vertical;
    }

}
package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.FontType;

import java.io.File;
import java.util.HashMap;



public class RenderAndroid implements IRender {

    private SurfaceView myView;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;

    private HashMap<String, ImageAndroid> images;
    private HashMap<String, FontAndroid> fonts;
    private AssetManager assetManager;
    private boolean verticalScreen;
    private boolean changedScreen;
    private int posCanvasX, posCanvasY;

    private int baseWidth;
    private int baseHeight;
    private float scale;


    public RenderAndroid(SurfaceView myView, AssetManager aM) {
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.assetManager = aM;
        this.paint = new Paint();
        this.fonts = new HashMap<>();
        this.images = new HashMap<>();
        this.verticalScreen = true;
        this.changedScreen = false;
        this.baseWidth = 1080;
        this.baseHeight = 1620;
        this.scale = 4.0f/6.0f;
    }

    public void scaleApp() {
        while (!this.surfaceValid());
        this.canvas = this.holder.lockCanvas();
        //400x600
        //x----y
        float w = holder.getSurfaceFrame().width();
        float y = holder.getSurfaceFrame().height();
        float scaleX = w;
        float scaleY = y;
        if (scaleX * scale < scaleY) scaleY = scaleX / scale;
        else scaleX = scaleY * scale;
        //canvas.translate((w - scaleX) / 2, (y - scaleY)/2);
        posCanvasX = (int) (w - scaleX) / 2;
        posCanvasY = (int) (y - scaleY) / 2;
        this.baseWidth = (int)scaleX;
        this.baseHeight = (int)scaleY;
        this.holder.unlockCanvasAndPost(canvas);
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
        canvas.drawBitmap(images.get(imageID).getImage(), x, y, paint);
    }

    @Override
    public void drawText(int x, int y, String text) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(text, x, y, paint);
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
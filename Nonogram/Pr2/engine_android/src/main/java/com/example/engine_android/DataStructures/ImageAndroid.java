package com.example.engine_android.DataStructures;

import android.content.res.AssetManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class ImageAndroid {
    // data of the image
    Bitmap image;

    public ImageAndroid(String path, AssetManager assetManager) {
        // loads the image and stores its info
        try {
            InputStream is = assetManager.open(path);
            this.image = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            System.err.println("Couldn't load image file");
            e.printStackTrace();
        }
    }

    public Bitmap getImage() { return this.image; }

    public int getWidth() { return this.image.getWidth(); }

    public int getHeight() { return this.image.getHeight(); }
}

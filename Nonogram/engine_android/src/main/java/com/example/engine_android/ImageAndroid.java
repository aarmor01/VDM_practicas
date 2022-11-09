package com.example.engine_android;

import com.example.engine_common.interfaces.IImage;

import java.io.InputStream;

import android.content.res.AssetManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageAndroid implements IImage {
    // data of the image
    Bitmap image;

    public ImageAndroid(String path, AssetManager assetManager) {
        // loads the image and stores its info
        try {
            InputStream is = assetManager.open(path);
            image = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            System.err.println("Couldn't load image file");
            e.printStackTrace();
        }
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}

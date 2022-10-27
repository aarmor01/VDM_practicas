package com.example.engine_desktop;

import com.example.engine_common.IImage;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDesktop implements IImage {
    Image image;

    public ImageDesktop(String path) {
        image = ImageIO.read(new File(path));
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public void setWidth(int w);
    public void setHeight(int h);
}

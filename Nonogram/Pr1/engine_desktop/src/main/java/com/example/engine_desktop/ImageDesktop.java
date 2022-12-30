package com.example.engine_desktop;

import com.example.engine_common.interfaces.IImage;

import java.io.File;

import java.awt.Image;
import javax.imageio.ImageIO;

public class ImageDesktop implements IImage {
    private Image image;

    public ImageDesktop(File file) {
        try {
            this.image = ImageIO.read(file);
        } catch (Exception e) {
            System.err.println("Couldn't load image file");
            e.printStackTrace();
        }
    }

    public Image getImage() { return this.image; }

    @Override
    public int getWidth() { return this.image.getWidth(null); }

    @Override
    public int getHeight() { return this.image.getHeight(null); }
}
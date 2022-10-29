package com.example.engine_desktop;

import com.example.engine_common.interfaces.IImage;

import java.io.File;
import java.io.IOException;

import java.awt.Image;
import javax.imageio.ImageIO;

public class ImageDesktop implements IImage {
    Image image;

    public ImageDesktop(File file) {
        try {
            this.image = ImageIO.read(file);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return this.image.getWidth(null);
    }

    @Override
    public int getHeight() {
        return this.image.getHeight(null);
    }
}
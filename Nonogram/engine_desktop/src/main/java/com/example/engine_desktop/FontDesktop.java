package com.example.engine_desktop;

import com.example.engine_common.interfaces.IFont;
import com.example.engine_common.shared.FontType;

import java.awt.Font;

import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FontDesktop implements IFont {
    Font font;

    public FontDesktop(File file, FontType type, int size){
        try {
            InputStream is = new FileInputStream(file);
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(type.ordinal(), size);
        }
        catch(IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return font.getSize();
    }

    @Override
    public boolean isBold() {
        return font.isBold();
    }

    @Override
    public boolean isItalic() {
        return font.isItalic();
    }
}

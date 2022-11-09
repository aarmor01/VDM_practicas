package com.example.engine_desktop;

import com.example.engine_common.interfaces.IFont;

import com.example.engine_common.shared.FontType;

import java.awt.Font;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

public class FontDesktop implements IFont {
    private Font font;

    public FontDesktop(File file, FontType type, int size){
        try {
            InputStream is = new FileInputStream(file);
            this.font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(type.ordinal(), size);
        } catch (Exception e) {
            System.err.println("Couldn't load font file");
            e.printStackTrace();
        }
    }

    public Font getFont() { return this.font; }

    @Override
    public int getSize() { return this.font.getSize(); }

    @Override
    public boolean isBold() { return this.font.isBold(); }

    @Override
    public boolean isItalic() { return this.font.isItalic(); }
}

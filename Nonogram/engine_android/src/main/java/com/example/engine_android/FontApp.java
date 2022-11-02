package com.example.engine_android;

import android.graphics.Typeface;

import com.example.engine_common.interfaces.IFont;
import com.example.engine_common.shared.FontType;

import java.io.File;

public class FontApp implements IFont {
    Typeface font;
    int size;

    public FontApp(File file, int s, FontType t) {
        switch (t) {
            case DEFAULT:
                break;
            case BOLD:
                font = Typeface.create(Typeface.createFromFile(file), Typeface.BOLD);
                break;
            case ITALIC:
                font = Typeface.create(Typeface.createFromFile(file), Typeface.ITALIC);
                break;
        }
        size = s;
    }

    public Typeface getFont() {
        return font;
    }

    @Override
    public int getSize() {
        return size;
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

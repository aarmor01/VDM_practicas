package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.example.engine_common.interfaces.IFont;
import com.example.engine_common.shared.FontType;

import java.io.File;

public class FontAndroid implements IFont {
    Typeface font;
    int size;

    public FontAndroid(String filePath, AssetManager aMan, int s, FontType t) {
        font = Typeface.create(Typeface.createFromAsset(aMan, filePath), t.ordinal());
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

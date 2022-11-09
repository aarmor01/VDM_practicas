package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.example.engine_common.interfaces.IFont;
import com.example.engine_common.shared.FontType;

import java.io.File;

public class FontAndroid implements IFont {
    //parameters to store the font info (type and size)
    private Typeface font;
    private int size;

    public FontAndroid(String filePath, AssetManager aMan, int s, FontType t) {
        //creates a font using an already existing font in the assets folder,
        //and an enum that identifies bold, italic or default
        font = Typeface.create(Typeface.createFromAsset(aMan, filePath), t.ordinal());
        //stores the size of the font
        size = s;
    }

    //getters of the font parameters
    public Typeface getFont() {
        return font;
    }

    @Override
    public int getSize() {
        return size;
    }

    //returns if the font modifier bold or italic are activated
    @Override
    public boolean isBold() {
        return font.isBold();
    }

    @Override
    public boolean isItalic() {
        return font.isItalic();
    }
}

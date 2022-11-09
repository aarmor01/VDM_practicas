package com.example.engine_android;

import com.example.engine_common.interfaces.IFont;

import com.example.engine_common.shared.FontType;

import android.graphics.Typeface;

import android.content.res.AssetManager;

public class FontAndroid implements IFont {
    // font data (type and size)
    private Typeface font;
    private int size;

    public FontAndroid(String filePath, AssetManager aMan, int s, FontType t) {
        // creates a font using a font asset in the assets folder,
        // and an enum that identifies it's type (bold, italic or default)
        this.font = Typeface.create(Typeface.createFromAsset(aMan, filePath), t.ordinal());
        this.size = s; // store size of the font
    }

    public Typeface getFont() { return this.font; }

    @Override
    public int getSize() { return this.size; }

    // returns if the font modifier bold is activated
    @Override
    public boolean isBold() { return this.font.isBold(); }

    // returns if the font modifier italic is activated
    @Override
    public boolean isItalic() { return this.font.isItalic(); }
}

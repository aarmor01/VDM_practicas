package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.example.engine_common.IFont;

public class FontApp implements IFont {
    Typeface font;

    public FontApp(String path, AssetManager assetManager) {
        font = Typeface.createFromAsset(assetManager, path);
    }

    public Typeface getFont() {
        return font;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean isBold() {
        return false;
    }

    @Override
    public boolean isItalic() {
        return false;
    }

    @Override
    public void setSize(int s) {

    }

    @Override
    public void setBold(boolean bold) {

    }

    @Override
    public void setItalic(boolean italic) {

    }
}

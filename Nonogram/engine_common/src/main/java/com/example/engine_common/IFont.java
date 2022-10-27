package com.example.engine_common;

public interface IFont {
    public int getSize();
    public boolean isBold();
    public boolean isItalic();

    public void setSize(int s);
    public void setBold(boolean bold);
    public void setItalic(boolean italic);
}

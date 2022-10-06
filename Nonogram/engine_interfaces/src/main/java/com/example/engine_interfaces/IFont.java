package com.example.engine_interfaces;

public interface IFont {
    public int getSize();
    public String getText();
    public String getPath();
    public boolean isBold();
    public boolean isItalic();

    public void setSize(int s);
    public void setText(String t);
    public void setBold(boolean bold);
    public void setItalic(boolean italic);
}

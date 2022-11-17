package com.example.engine_android;

public class InputAndroid {
    private int x, y;       // input coords
    private InputType type; // input type
    private int id;         // input ID

    public InputAndroid(int x, int y, InputType type, int id) { this.x = x; this.y = y; this.type = type; this.id = id; }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public InputType getType() { return this.type; }

    public int getID() { return this.id; }
}

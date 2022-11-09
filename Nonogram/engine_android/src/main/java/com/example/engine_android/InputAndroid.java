package com.example.engine_android;

import com.example.engine_common.interfaces.IInput;

import com.example.engine_common.shared.InputType;

public class InputAndroid implements IInput {
    private int x, y;       // input coords
    private InputType type; // input type
    private int id;         // input ID

    public InputAndroid(int x, int y, InputType type, int id) { this.x = x; this.y = y; this.type = type; this.id = id; }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public InputType getType() {
        return type;
    }

    @Override
    public int getID() {
        return id;
    }
}

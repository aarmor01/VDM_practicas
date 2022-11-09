package com.example.engine_desktop;

import com.example.engine_common.interfaces.IInput;

import com.example.engine_common.shared.InputType;

public class InputDesktop implements IInput {
    private int x, y;       // input coords
    private InputType type; // input type

    InputDesktop(int x, int y, InputType type) { this.x = x;  this.y = y; this.type = type; }

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
        return 0;
    }
}

package com.example.engine_android;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.shared.InputType;

public class InputAndroid implements IInput {
    int x, y, iD;
    InputType type;

    public InputAndroid(int x_, int y_, InputType t_, int iD_) {
        x = x_;
        y = y_;
        type = t_;
        iD = iD_;
    }

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
        return iD;
    }
}

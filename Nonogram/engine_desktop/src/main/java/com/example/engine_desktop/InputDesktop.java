package com.example.engine_desktop;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.shared.InputType;

public class InputDesktop implements IInput {
    int x, y;
    InputType type;

    InputDesktop(int x_, int y_, InputType t_) {
        x = x_;
        y = y_;
        type = t_;
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
        return 0;
    }
}

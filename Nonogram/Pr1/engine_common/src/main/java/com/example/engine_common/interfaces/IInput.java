package com.example.engine_common.interfaces;

import com.example.engine_common.shared.InputType;

public interface IInput {
    public int getX();
    public int getY();
    public InputType getType();
    public int getID();
}
package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IInput;

import java.util.LinkedList;

public class InputManager {
    LinkedList<IInput> bufferInput;

    public InputManager() { bufferInput = new LinkedList<>(); }

    public void addInput(IInput newInput) { bufferInput.addLast(newInput); }

    public IInput getInput() { return bufferInput.removeFirst(); }
}

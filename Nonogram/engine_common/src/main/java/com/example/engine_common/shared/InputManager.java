package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IInput;

import java.util.Queue;

public class InputManager {
    Queue<IInput> bufferInput;

    public void addInput(IInput newInput) { bufferInput.add(newInput); }

    public IInput getInput() { return bufferInput.remove(); }
}

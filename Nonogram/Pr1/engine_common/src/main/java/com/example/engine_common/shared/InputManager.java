package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IInput;

import java.util.LinkedList;

public class InputManager {
    private LinkedList<IInput> bufferInput;

    public InputManager() { this.bufferInput = new LinkedList<>(); }

    public void addInput(IInput newInput) { this.bufferInput.addLast(newInput); }

    public LinkedList<IInput> getInput() {
        LinkedList<IInput> bufferCopy = new LinkedList<>(this.bufferInput);
        this.bufferInput.clear();
        return bufferCopy;
    }
}

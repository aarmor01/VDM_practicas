package com.example.engine_common.shared;

import com.example.engine_common.interfaces.IInput;

import java.util.LinkedList;

import sun.awt.image.ImageWatched;

public class InputManager {
    LinkedList<IInput> bufferInput;

    public InputManager() { bufferInput = new LinkedList<>(); }

    public void addInput(IInput newInput) { bufferInput.addLast(newInput); }

    public LinkedList<IInput> getInput() {
        LinkedList<IInput> bufferCopy = new LinkedList<>(bufferInput);
        bufferInput.clear();
        return bufferCopy;
    }
}

package com.example.engine_android.Modules;

import com.example.engine_android.DataStructures.InputAndroid;

import java.util.LinkedList;

public class InputManager {
    private LinkedList<InputAndroid> bufferInput;

    public InputManager() { this.bufferInput = new LinkedList<>(); }

    public void addInput(InputAndroid newInput) { this.bufferInput.addLast(newInput); }

    public LinkedList<InputAndroid> getInput() {
        LinkedList<InputAndroid> bufferCopy = new LinkedList<>(bufferInput);
        this.bufferInput.clear();
        return bufferCopy;
    }
}

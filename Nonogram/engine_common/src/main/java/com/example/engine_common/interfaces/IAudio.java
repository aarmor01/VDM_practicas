package com.example.engine_common.interfaces;

import java.io.IOException;

public interface IAudio {
    String loadSound(String filePath) throws IOException;
    void playSound(String soundName);
}

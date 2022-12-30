package com.example.nonogram;

public class GameManager {
    // singleton
    private static GameManager instance = null;

    // dimensions
    private int gameWidth, gameHeight;

    // ---------------- SINGLETON ----------------
    public GameManager() {

    }

    // loads the corresponding CategoryData saved files
    public static void init(int w, int h) {
        if (instance == null)
            // singleton initialization
            instance = new GameManager();

        // instance setting
        instance.setup(w, h);
    }

    public static GameManager getInstance() {
        return instance;
    }

    // saves CategoryData data
    public static void shutdown() {
        if (instance != null)
            // delete instance
            instance = null;
    }

    private void setup(int w, int h) {
        this.gameWidth = w;
        this.gameHeight = h;
    }

    // ---------------- DIMENSIONS ----------------
    public int getWidth() {
        return this.gameWidth;
    }

    public int getHeight() {
        return this.gameHeight;
    }
}

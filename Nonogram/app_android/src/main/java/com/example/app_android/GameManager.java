package com.example.app_android;

import com.example.app_android.Objects.Board;
import com.example.app_android.Objects.CategoryData;

import com.example.engine_android.EngineAndroid;

import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameManager {
    public enum ColorTypes{bgColor, mainColor, secondaryColor, auxColor}

    // some const parameters
    final String SAVE_FILE_EXTENSION = ".bin";
    final String FREE_LEVEL_SAVE_NAME = "save_free";
    final String STORY_LEVEL_SAVE_NAME = "save_story_";  // add the level id after this when using it
    final int NUM_LEVELS = 4;
    final public int NUM_PALETTES = 3;
    final int NUM_COLORS_PER_PALETTE = 4;

    // singleton
    private static GameManager instance = null;

    // categories (0 FREE LEVEL, 1-4 STORY LEVELS)
    CategoryData[] levels = null;

    //Palettes data
    private int[][] palettes;
    private boolean[] unlockedPalettes;
    private int idActPalette;

    //Coins
    private int coins;

    public GameManager() {
    }

    // loads the corresponding CategoryData saved files
    public static void init(EngineAndroid engine, Bundle savedState) {
        // singleton initialization
        instance = new GameManager();

        // instance setting
        instance.setup(engine, savedState);
    }

    public static GameManager getInstance() {
        return instance;
    }

    // saves CategoryData data
    public static void shutdown(EngineAndroid engine, Bundle savedState) {
        // instance closure
        instance.close(engine, savedState);

        // delete instance
        instance = null;
    }

    public int getLevelUnlocked(int category) {
        return this.levels[category].levelUnlocked;
    }

    public Board getSavedBoard(int category) {
        return this.levels[category].pendingBoard;
    }

    public void updateCategory(int category, int level, Board pendBoard) {
        if (pendBoard != null)
            this.levels[category].pendingBoard = pendBoard;

        if (level - this.levels[category].levelUnlocked == 1)
            this.levels[category].levelUnlocked = level;
    }

    public void resetBoard(int category) {
        this.levels[category].pendingBoard = null;
    }

    private void setup(EngineAndroid engine, Bundle savedState) {
        initPalettes();

        // +1 indicates always a free level
        this.levels = new CategoryData[this.NUM_LEVELS + 1];

        // deserialize free & story levels
        for (int level = 0; level < this.NUM_LEVELS + 1; level++)
            loadCategory(engine, savedState, level);

        coins = 0;
    }

    private void close(EngineAndroid engine, Bundle savedState) {
        // serialize free & story levels
        for (int level = 0; level < this.NUM_LEVELS + 1; level++)
            storeCategory(engine, savedState, level);
    }

    private void loadCategory(EngineAndroid engine, Bundle savedState, int category) {
        // name and file of the category for serialization
        String category_name = category == 0 ? FREE_LEVEL_SAVE_NAME : STORY_LEVEL_SAVE_NAME + category,
                category_file = category_name + SAVE_FILE_EXTENSION;

        // first, we try to load it from bundle
        if (savedState != null) {
            this.levels[category] = (CategoryData) savedState.getSerializable(category_name);
            if (this.levels[category] != null)
                return;
        }

        // if it doesn't work, we load from file
        try {
            // Reading the object from a file
            FileInputStream file = engine.openInputFile(category_file);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            this.levels[category] = (CategoryData) in.readObject();
            System.out.println("Category " + category + " has been deserialized.");

            // close
            in.close();
            file.close();
        } catch (Exception ex) { // if file also fails, we end up creating new data
            System.out.println("Category not serialised previously. Loading new Category");
            this.levels[category] = new CategoryData();
            if (category == 1) // we set the first story level with the first level unlocked
                this.levels[category].levelUnlocked++;
        }

        // we remove the file so we make sure it has to be
        // saved on GameManager shutdown
        engine.removeFile(category_file);
    }

    private void storeCategory(EngineAndroid engine, Bundle savedState, int category) {
        // name and file of the category for serialization
        String category_name = category == 0 ? FREE_LEVEL_SAVE_NAME : STORY_LEVEL_SAVE_NAME + category,
                category_file = category_name + SAVE_FILE_EXTENSION;

        // first, we save it from bundle
        if (savedState != null)
            savedState.putSerializable(category_name, this.levels[category]);

        // then, we save it on a file just in case
        try {
            // Reading the object to a file
            FileOutputStream file = engine.openOutputFile(category_file);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(this.levels[category]);
            System.out.println("Category " + category + " has been serialized.");

            // close
            out.close();
            file.close();
        } catch (Exception ex) {
            System.out.println("Category not serialised correctly. Serializing new empty Category");
        }
    }

    private void initPalettes() {
        palettes = new int[NUM_PALETTES][NUM_COLORS_PER_PALETTE];
        palettes[0][ColorTypes.bgColor.ordinal()] = 0xFFFFFFFF;//Background
        palettes[0][ColorTypes.mainColor.ordinal()] = 0xFF0000FF;//CellsCorrect
        palettes[0][ColorTypes.secondaryColor.ordinal()] = 0xFFFF0000;//CellsFailed
        palettes[0][ColorTypes.auxColor.ordinal()] = 0xFFCCCCCC;//CellsNotMarked

        palettes[1][ColorTypes.bgColor.ordinal()] = 0xFF008800;
        palettes[1][ColorTypes.mainColor.ordinal()] = 0xFF00FF00;
        palettes[1][ColorTypes.secondaryColor.ordinal()] = 0xFFFF00FF;
        palettes[1][ColorTypes.auxColor.ordinal()] = 0xFF0000FF;

        palettes[2][ColorTypes.bgColor.ordinal()] = 0xFF660000;
        palettes[2][ColorTypes.mainColor.ordinal()] = 0xFFFF0000;
        palettes[2][ColorTypes.secondaryColor.ordinal()] = 0xFF00FF00;
        palettes[2][ColorTypes.auxColor.ordinal()] = 0xFF00FF44;

        unlockedPalettes = new boolean[NUM_PALETTES];
        unlockedPalettes[0] = true;
        for(int i=1; i<NUM_PALETTES; i++)
            unlockedPalettes[i] = false;
        idActPalette = 0;
    }

    public int getColor(int colorType){ return palettes[idActPalette][colorType]; }

    public int getActPalette() { return idActPalette; }

    public boolean isPaletteUnlocked(int pId){ return unlockedPalettes[pId]; }

    public void unlockPalette(int pId) { unlockedPalettes[pId] = true; }

    public void setPalette(int pId) { idActPalette = pId; }

    public int getCoins(){ return coins; }

    public void addCoins(int c){ coins += c; }
}

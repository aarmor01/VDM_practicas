package com.example.app_android;

import com.example.app_android.Objects.CategoryData;

import com.example.engine_android.EngineAndroid;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameManager {
    public enum ColorTypes {BG_COLOR, MAIN_COLOR, SECONDARY_COLOR, AUX_COLOR}

    // some const parameters
    private final int NUM_CATEGORIES = 5;
    public final int NUM_PALETTES = 3;
    private final int NUM_COLORS_PER_PALETTE = 4;

    final String SAVE_FILE = "save_data.json";

    // singleton
    private static GameManager instance = null;

    // categories (0 FREE LEVEL, 1-4 STORY LEVELS)
    private CategoryData[] categories = null;   // save

    // palettes data
    private int[][] palettes;
    private boolean[] unlockedPalettes;         // save
    private int currentPalette;                 // save

    // coins
    private int coins;                          // save

    // ---------------- SINGLETON ----------------
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

    private void setup(EngineAndroid engine, Bundle savedState) {
        loadGameDefaultData();

        // file to be read from
        try {
            FileInputStream file = engine.openInputFile(SAVE_FILE);
            ObjectInputStream readSaveFile = new ObjectInputStream(file);

            // load all game data
            loadData(savedState, readSaveFile);

            readSaveFile.close();
            file.close();
        } catch (Exception ex) {
            System.out.println("Save file doesn't exist [Load].");
            ex.printStackTrace();
            loadGameDefaultData();
        }
    }

    private void close(EngineAndroid engine, Bundle savedState) {
        // file to be written to
        try {
            FileOutputStream file = engine.openOutputFile(SAVE_FILE);
            ObjectOutputStream writeSaveFile = new ObjectOutputStream(file);

            // store all game data
            storeData(savedState, writeSaveFile);

            writeSaveFile.close();
            file.close();
        } catch (Exception ex) {
            System.out.println("Save file doesn't exist [Store].");
            ex.printStackTrace();
        }
    }

    // ---------------- CATEGORIES ----------------
    private void loadGameDefaultData() {
        this.coins = 0;
        this.currentPalette = 0;

        this.unlockedPalettes = new boolean[this.NUM_PALETTES];
        this.unlockedPalettes[0] = true;
        for (int palette = 1; palette < this.NUM_PALETTES; palette++)
            this.unlockedPalettes[palette] = false;
        loadPalettes();

        this.categories = new CategoryData[this.NUM_CATEGORIES];
        for (int ct = 0; ct < this.categories.length; ct++) {
            this.categories[ct] = new CategoryData();
            this.categories[ct].levelUnlocked = ct < 2 ? 0 : -1;
            this.categories[ct].pendingBoardLevel = -1;
            this.categories[ct].pendingBoardState = null;
            this.categories[ct].pendingBoardLives = -1;
        }
    }

    private void loadData(Bundle savedState, ObjectInputStream readSaveFile) {
        try {
            JSONObject dataObject = new JSONObject(readSaveFile.readObject().toString());
            this.coins = dataObject.getInt("coins");
            this.currentPalette = dataObject.getInt("currentPalette");

            JSONArray palettes = dataObject.getJSONArray("unlockedPalettes");
            for (int palette = 0; palette < palettes.length(); palette++)
                this.unlockedPalettes[palette] = palettes.getBoolean(palette);

            JSONArray categories = dataObject.getJSONArray("categories");
            for (int ct = 0; ct < this.categories.length; ct++) {
                JSONObject categoryData = categories.getJSONObject(ct);

                this.categories[ct].levelUnlocked = categoryData.getInt("levelUnlocked");
                this.categories[ct].pendingBoardLevel = categoryData.getInt("pendingBoardLevel");

                if (this.categories[ct].pendingBoardLevel != -1) {
                    this.categories[ct].pendingBoardLives = categoryData.getInt("pendingBoardLives");

                    int boardRows = categoryData.getInt("pendingBoardRow");
                    int boardCols = categoryData.getInt("pendingBoardCol");
                    JSONArray boardState = categoryData.getJSONArray("pendingBoardState");
                    this.categories[ct].pendingBoardState = new int[boardRows][boardCols];
                    for (int row = 0; row < boardRows; row++)
                        for (int col = 0; col < boardCols; col++)
                            this.categories[ct].pendingBoardState[row][col] = boardState.getInt((boardCols * row) + col);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data.");
            e.printStackTrace();
        }
//              if (savedState != null) {
//                this.categories[category] = (CategoryData) savedState.getSerializable(category_name);
//                if (this.categories[category] != null)
//                    return;
//            }
//        // if it doesn't work, we load from file
//        try {
//            // Reading the object from a file
//            FileInputStream file = engine.openInputFile(category_file);
//            ObjectInputStream in = new ObjectInputStream(file);
//
//            // Method for deserialization of object
//            this.categories[category] = (CategoryData) in.readObject();
//            System.out.println("Category " + category + " has been deserialized.");
//
//            // close
//            in.close();
//            file.close();
//        } catch (Exception ex) { // if file also fails, we end up creating new data
//            System.out.println("Category not serialised previously. Loading new Category");
//            this.categories[category] = new CategoryData();
//            if (category == 1) // we set the first story level with the first level unlocked
//                this.categories[category].levelUnlocked++;
//        }
//
//        // we remove the file so we make sure it has to be
//        // saved on GameManager shutdown
//        engine.removeFile(category_file);
    }

    private void storeData(Bundle savedState, ObjectOutputStream writeSaveFile) {
        try {
            JSONObject dataObject = new JSONObject();
            dataObject.put("coins", this.coins);
            dataObject.put("currentPalette", this.currentPalette);

            JSONArray palettes = new JSONArray();
            for (int palette = 0; palette < this.NUM_PALETTES; palette++)
                palettes.put(this.unlockedPalettes[palette]);
            dataObject.put("unlockedPalettes", palettes);

            JSONArray categories = new JSONArray();
            for (int ct = 0; ct < this.categories.length; ct++) {
                JSONObject categoryObject = new JSONObject();
                categoryObject.put("levelUnlocked", this.categories[ct].levelUnlocked);
                categoryObject.put("pendingBoardLevel", this.categories[ct].pendingBoardLevel);

                if (this.categories[ct].pendingBoardLevel != -1) {
                    categoryObject.put("pendingBoardLives", this.categories[ct].pendingBoardLives);

                    categoryObject.put("pendingBoardRow", this.categories[ct].pendingBoardState.length);
                    categoryObject.put("pendingBoardCol", this.categories[ct].pendingBoardState[0].length);

                    JSONArray cells = new JSONArray();
                    for (int row = 0; row < this.categories[ct].pendingBoardState.length; row++)
                        for (int col = 0; col < this.categories[ct].pendingBoardState[0].length; col++)
                            cells.put(this.categories[ct].pendingBoardState[row][col]);
                    categoryObject.put("pendingBoardState", cells);
                }

                categories.put(categoryObject);
            }
            dataObject.put("categories", categories);

            writeSaveFile.writeObject(dataObject.toString());
        } catch (Exception e) {
            System.out.println("Error storing data.");
            e.printStackTrace();
        }
//        // name and file of the category for serialization
//        String category_name = CATEGORY_SAVE_NAME + category,
//               category_file = category_name + SAVE_FILE_EXTENSION;
//
//        // first, we save it from bundle
//        if (savedState != null)
//            savedState.putSerializable(category_name, this.categories[category]);
//
//        // then, we save it on a file just in case
//        try {
//            // Reading the object to a file
//            FileOutputStream file = engine.openOutputFile(category_file);
//            ObjectOutputStream out = new ObjectOutputStream(file);
//
//            // Method for serialization of object
//            out.writeObject(this.categories[category]);
//            System.out.println("Category " + category + " has been serialized.");
//
//            // close
//            out.close();
//            file.close();
//        } catch (Exception ex) {
//            System.out.println("Category not serialised correctly. Serializing new empty Category");
//        }
    }

    public void updateCategory(int category, int level, int[][] pendBoard, int lives) {
        if (pendBoard != null) {
            this.categories[category].pendingBoardLevel = level;
            this.categories[category].pendingBoardState = pendBoard;
            this.categories[category].pendingBoardLives = lives;
        }

        if (level - this.categories[category].levelUnlocked == 1)
            this.categories[category].levelUnlocked = level;
    }

    public void resetBoard(int category) {
        this.categories[category].pendingBoardLevel = -1;
        this.categories[category].pendingBoardState = null;
        this.categories[category].pendingBoardLives = -1;
    }

    public int getLevelUnlocked(int category) {
        return this.categories[category].levelUnlocked;
    }

    public int getSavedBoardLevel(int category) {
        return this.categories[category].pendingBoardLevel;
    }

    public int[][] getSavedBoardState(int category) {
        return this.categories[category].pendingBoardState;
    }

    public int getSavedBoardLives(int category) {
        return this.categories[category].pendingBoardLives;
    }

    // ---------------- PALETTES ----------------
    private void loadPalettes() {
        palettes = new int[NUM_PALETTES][NUM_COLORS_PER_PALETTE];
        palettes[0][ColorTypes.BG_COLOR.ordinal()] = 0xFFFFFFFF;//Background
        palettes[0][ColorTypes.MAIN_COLOR.ordinal()] = 0xFF0000FF;//CellsCorrect
        palettes[0][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFFFF0000;//CellsFailed
        palettes[0][ColorTypes.AUX_COLOR.ordinal()] = 0xFFCCCCCC;//CellsNotMarked

        palettes[1][ColorTypes.BG_COLOR.ordinal()] = 0xFF008800;
        palettes[1][ColorTypes.MAIN_COLOR.ordinal()] = 0xFF00FF00;
        palettes[1][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFFFF00FF;
        palettes[1][ColorTypes.AUX_COLOR.ordinal()] = 0xFF0000FF;

        palettes[2][ColorTypes.BG_COLOR.ordinal()] = 0xFF660000;
        palettes[2][ColorTypes.MAIN_COLOR.ordinal()] = 0xFFFF0000;
        palettes[2][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFF00FF00;
        palettes[2][ColorTypes.AUX_COLOR.ordinal()] = 0xFF00FF44;

//        unlockedPalettes = new boolean[NUM_PALETTES];
//        unlockedPalettes[0] = true;
//        for(int i=1; i<NUM_PALETTES; i++)
//            unlockedPalettes[i] = false;
//        currentPalette = 0;
    }

    public int getColor(int colorType) {
        return palettes[currentPalette][colorType];
    }

    public int getActPalette() {
        return currentPalette;
    }

    public boolean isPaletteUnlocked(int pId) {
        return unlockedPalettes[pId];
    }

    public void unlockPalette(int pId) {
        unlockedPalettes[pId] = true;
    }

    public void setPalette(int pId) {
        currentPalette = pId;
    }

    // ---------------- COINS ----------------
    public int getCoins() {
        return coins;
    }

    public void addCoins(int c) {
        coins += c;
    }
}

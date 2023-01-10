package com.example.app_android;

import com.example.app_android.Objects.CategoryData;

import com.example.app_android.Scenes.BoardScene;
import com.example.engine_android.EngineAndroid;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class GameManager {
    public enum ColorTypes {BG_COLOR, MAIN_COLOR, SECONDARY_COLOR, AUX_COLOR}

    // some const parameters
    private final int NUM_CATEGORIES = 5;
    public final int NUM_PALETTES = 3;
    private final int NUM_COLORS_PER_PALETTE = 4;
    private final int LIGHTMODES = 2;

    private final String SAVE_FILE = "save_data.json";
    private final String CHECKSUM_FILE = "checksum.txt";

    // singleton
    private static GameManager instance = null;

    // dimensions
    private int width, height;

    // categories (0 FREE LEVEL, 1-4 STORY LEVELS)
    private CategoryData[] categories = null;   // save

    // palettes data
    private int[][][] palettes;
    private boolean[] unlockedPalettes;         // save
    private int currentPalette;                 // save
    private int nightMode;

    // coins
    private int coins;                          // save

    // sensor
    private LightSensorApp lightSensor;

    // ---------------- SINGLETON ----------------
    private GameManager() {

    }

    // loads the corresponding CategoryData saved files
    public static void init(EngineAndroid engine, int w, int h) {
        if (instance == null)
            // singleton initialization
            instance = new GameManager();

        // instance setting
        instance.setup(engine, w, h);
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

    public static void load(EngineAndroid engine, Bundle savedState) {
        // data save
        instance.open(engine, savedState);
    }

    public static void save(EngineAndroid engine, Bundle savedState) {
        // data save
        instance.close(engine, savedState);
    }

    private void setup(EngineAndroid engine, int w, int h) {
        // start lightSensor
        this.lightSensor = new LightSensorApp(engine.getContext(), engine);
        engine.setLightSensor(this.lightSensor);

        // assign dimensions
        this.width = w;
        this.height = h;

        // load default values, in case something goes wrong
        loadGameDefaultData();
    }

    // ---------------- SAVE DATA MANAGEMENT ----------------
    private void open(EngineAndroid engine, Bundle savedState) {
        // try to load from bundle
        if (loadData(savedState)) {
            System.out.println("Loaded properly from bundle");
            return;
        }

        // file to be read from
        try {
            FileInputStream fileInputStream = engine.openInputFile(SAVE_FILE);
            FileInputStream checksumFile = engine.openInputFile(CHECKSUM_FILE);

            // check if file has been modified
            String savedChecksum = loadChecksum(checksumFile);
            String actualChecksum = engine.getChecksum(fileInputStream);
            if (!savedChecksum.equals(actualChecksum)) {
                System.out.println("Save file has been modified, so we discard it");
                return;
            }
            fileInputStream.close();

            // input stream object
            fileInputStream = engine.openInputFile(SAVE_FILE);
            ObjectInputStream readSaveFile = new ObjectInputStream(fileInputStream);

            // load all game data
            loadData(readSaveFile);

            readSaveFile.close();
            fileInputStream.close();
        } catch (Exception ex) {
            System.out.println("Save file doesn't exist [Load].");
            ex.printStackTrace();
            loadGameDefaultData();
        }
    }

    private void close(EngineAndroid engine, Bundle savedState) {
        // if we are on BoardScene, we save the state anyways
        if (engine.getSceneManager().currentScene().getId() == "BoardScene")
            ((BoardScene) engine.getSceneManager().currentScene()).updateCategoryInformation();

        // store on bundle (made to persist through config changes)
        storeData(savedState);

        // file to be written to
        try {
            FileOutputStream file = engine.openOutputFile(SAVE_FILE);
            ObjectOutputStream writeSaveFile = new ObjectOutputStream(file);

            // store all game data
            storeData(writeSaveFile);

            writeSaveFile.close();
            file.close();

            // save checksum
            saveChecksum(engine);
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

    // ---------------- SAVE DATA MANAGEMENT ----------------
    private void loadData(ObjectInputStream readSaveFile) {
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
    }

    private boolean loadData(Bundle savedState) {
        if (savedState == null)
            return false;

        this.coins = savedState.getInt("coins");
        this.currentPalette = savedState.getInt("currentPalette");

        for (int palette = 0; palette < this.NUM_PALETTES; palette++)
            this.unlockedPalettes[palette] = savedState.getBoolean("unlockedPalettes_" + palette);

        for (int ct = 0; ct < this.categories.length; ct++) {
            this.categories[ct] = (CategoryData) savedState.getSerializable("category_" + ct);
            if (this.categories[ct] == null)
                return false;
        }

        return true;
    }

    private void storeData(ObjectOutputStream writeSaveFile) {
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
    }

    private void storeData(Bundle savedState) {
        savedState.putInt("coins", this.coins);
        savedState.putInt("currentPalette", this.currentPalette);

        for (int palette = 0; palette < this.NUM_PALETTES; palette++)
            savedState.putBoolean("unlockedPalettes_" + palette, this.unlockedPalettes[palette]);

        for (int ct = 0; ct < this.categories.length; ct++)
            savedState.putSerializable("category_" + ct, this.categories[ct]);
    }

    private String loadChecksum(FileInputStream checksumFile) throws IOException {
        ByteArrayOutputStream resultChecksum = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192]; int length;
        while ((length = checksumFile.read(buffer)) != -1)
            resultChecksum.write(buffer, 0, length);

        return resultChecksum.toString("UTF-8");
    }

    private void saveChecksum(EngineAndroid engine) throws NoSuchAlgorithmException, IOException {
        FileOutputStream fileOutputStream = engine.openOutputFile(CHECKSUM_FILE);
        FileInputStream fileInputStream = engine.openInputFile(SAVE_FILE);

        // save obtained checksum
        String checksum = engine.getChecksum(fileInputStream);
        fileOutputStream.write(checksum.getBytes(StandardCharsets.UTF_8));

        fileInputStream.close();
        fileOutputStream.close();
    }

    // ---------------- CATEGORIES ----------------
    public void updateCategory(int category, int level, int[][] pendBoard, int lives) {
        if (category != 0 && pendBoard != null) {
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
        nightMode = 0;
        palettes = new int[LIGHTMODES][NUM_PALETTES][NUM_COLORS_PER_PALETTE];
        palettes[0][0][ColorTypes.BG_COLOR.ordinal()] = 0xFFFFFFFF;//Background
        palettes[0][0][ColorTypes.MAIN_COLOR.ordinal()] = 0xFF5564eb;//CellsCorrect
        palettes[0][0][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFFFF0000;//CellsFailed
        palettes[0][0][ColorTypes.AUX_COLOR.ordinal()] = 0xFFCCCCCC;//CellsNotMarked

        palettes[1][0][ColorTypes.BG_COLOR.ordinal()] = 0xFFAAAAAA;//Background
        palettes[1][0][ColorTypes.MAIN_COLOR.ordinal()] = 0xFF6d7aee;//CellsCorrect
        palettes[1][0][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFFCC0000;//CellsFailed
        palettes[1][0][ColorTypes.AUX_COLOR.ordinal()] = 0xFFCCCCCC;//CellsNotMarked

        palettes[0][1][ColorTypes.BG_COLOR.ordinal()] = 0xFFEF9A3A;
        palettes[0][1][ColorTypes.MAIN_COLOR.ordinal()] = 0xFFC059DF;
        palettes[0][1][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFFD1421F;
        palettes[0][1][ColorTypes.AUX_COLOR.ordinal()] = 0xFF996F40;

        palettes[1][1][ColorTypes.BG_COLOR.ordinal()] = 0xFF996F40;//Background
        palettes[1][1][ColorTypes.MAIN_COLOR.ordinal()] = 0xFF9035AC;//CellsCorrect
        palettes[1][1][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFFB53111;//CellsFailed
        palettes[1][1][ColorTypes.AUX_COLOR.ordinal()] = 0xFFEF9A3A;//CellsNotMarked

        palettes[0][2][ColorTypes.BG_COLOR.ordinal()] = 0xFF3824E4;
        palettes[0][2][ColorTypes.MAIN_COLOR.ordinal()] = 0xFF0DC270;
        palettes[0][2][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFFB1254D;
        palettes[0][2][ColorTypes.AUX_COLOR.ordinal()] = 0xFF3A3092;

        palettes[1][2][ColorTypes.BG_COLOR.ordinal()] = 0xFF3A3092;//Background
        palettes[1][2][ColorTypes.MAIN_COLOR.ordinal()] = 0xFF2B9364;//CellsCorrect
        palettes[1][2][ColorTypes.SECONDARY_COLOR.ordinal()] = 0xFF80223D;//CellsFailed
        palettes[1][2][ColorTypes.AUX_COLOR.ordinal()] = 0xFF3824E4;//CellsNotMarked
    }

    public int getColor(int colorType) { return palettes[nightMode][currentPalette][colorType];}

    public void setNightMode(int nightMode_) { nightMode = nightMode_;}

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

    // ---------------- DIMENSIONS ----------------
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void flipDimensions(){
        int aux = height;
        height = width;
        width = aux;
    }
}

package com.example.app_android;

import android.content.Context;

import com.example.engine_android.EngineAndroid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameManager {
    // singleton
    private static GameManager instance = null;

    // engine
    EngineAndroid engine;

    // params
    CategoryData freeLevel;
    CategoryData[] storyLevels;
    final int numStoryLevels = 4;

    public GameManager() {}

    // loads the corresponding CategoryData saved files
    public static void init(EngineAndroid engine) {
        // singleton initialization
        instance = new GameManager();

        // setting some values
        instance.setup(engine);

        // deserialize free level
        instance.loadCategory("save_free.bin", -1);

        // deserialize story levels
        for (int level = 0; level < instance.numStoryLevels; level++)
            instance.loadCategory("save_story_" + level + ".bin", level);

        // aiuda
        instance.freeLevel.levelUnlocked++;
        instance.storyLevels[1].levelUnlocked++;
        instance.freeLevel.pendingBoard = new Board();
        instance.freeLevel.pendingBoard.win = true;
    }

    public static GameManager getInstance() {
        return instance;
    }

    // saves CategoryData data
    public static void shutdown() {
        // serialize free level
        instance.storeCategory("save_free.bin", -1);

        // serialize story levels
        for (int level = 0; level < instance.numStoryLevels; level++)
            instance.storeCategory("save_story_" + level + ".bin", level);

        // delete instance
        instance = null;
    }

    private void setup(EngineAndroid engine) {
        this.engine = engine;
        this.storyLevels = new CategoryData[this.numStoryLevels];
    }

    private void loadCategory(String path, int id) {
        CategoryData ct_temp = null;

        try
        {
            // Reading the object from a file
            FileInputStream file = engine.openInputFile(path);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            ct_temp = (CategoryData)in.readObject();
            System.out.println("Category " + id + " has been deserialized.");

            // close
            in.close();
            file.close();
        }
        catch(Exception ex) {
            System.out.println("Category not serialised previously. Loading new Category");
            ct_temp = new CategoryData();
        }

        if (id == -1) this.freeLevel = ct_temp;
        else this.storyLevels[id] = ct_temp;
    }

    private void storeCategory(String path, int id) {
        try
        {
            // Reading the object to a file
            FileOutputStream file = engine.openOutputFile(path);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            if (id == -1) out.writeObject(this.freeLevel);
            else out.writeObject(this.storyLevels[id]);
            System.out.println("Category " + id + " has been deserialized.");

            // close
            out.close();
            file.close();
        } catch(Exception ex) {
            System.out.println("Category not serialised previously. Loading new Category");
        }
    }
}

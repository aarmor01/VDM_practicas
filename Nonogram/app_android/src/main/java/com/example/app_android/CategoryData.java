package com.example.app_android;

import java.io.Serializable;

public class CategoryData implements Serializable {
    // -1 means category not unlocked
    int levelUnlocked = -1;
    // null if not currently on a board when exit
    Board pendingBoard;
}

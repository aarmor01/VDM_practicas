package com.example.app_android.Objects;

import java.io.Serializable;

public class CategoryData implements Serializable {
    // -1 means category not unlocked
    public int levelUnlocked = -1;
    // null if not currently on a board when exit
    public Board pendingBoard;
}

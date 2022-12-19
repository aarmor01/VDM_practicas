package com.example.app_android.Objects;

import java.io.Serializable;

public class CategoryData implements Serializable {
    // -1 means category not unlocked
    public int levelUnlocked = -1;
    // -1 if not currently on a board when exit
    public int pendingBoardLevel;
    // state of the board, if needed
    public int[][] pendingBoardState;
    // saved pendingBoardLives in case needed
    public int pendingBoardLives;
}

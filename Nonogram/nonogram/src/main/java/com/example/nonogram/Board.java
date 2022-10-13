package com.example.nonogram;

import java.util.Random;

public class Board {
    private Cell[][] board;
    private int cellsLeft;
    private int height, width;

    public void render(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                board[i][j].render();
            }
        }
    }

    public void init(int h, int w) {
        height = h;
        width = w;
        board = new Cell[height][width];
        Random random = new Random();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int rand = random.nextInt(10);
                board[i][j].init(rand<4);
                if (rand < 4) cellsLeft++;
            }
        }
    }

    public void markCell(int x, int y) {
        cellsLeft += board[x][y].changeCell();
    }
}

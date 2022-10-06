package com.example.nonogram;

public class Board {
    private Cell[][] board;
    private int cellsLeft;

    public void createBoard(int height, int width, int allCells) {
        board = new Cell[height][width];
        cellsLeft = allCells;
    }

    public void markCell(int x, int y) {
        cellsLeft += board[x][y].changeCell();
    }
}

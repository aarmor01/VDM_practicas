package com.example.nonogram;

import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.shared.FontType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    static float SECONDS_CHECKED = 1.5f;
    private final int cellProbability = 4;
    public boolean win = false;

    private Cell[][] board;
    private List<Integer>[] colsNums;
    private List<Integer>[] rowsNums;

    private int rows, cols;
    private int posX, posY;

    private int board_cell_size;
    private int separation_margin;
    private int fontSize;
    private int maxNumbers;

    private int cellsLeft;

    private Label wrongCells;
    private Label missingCells;

    private List<Cell> checkedCells;
    private double lastTimeChecked;

    public void init(int w, int h) {
        this.cols = w;
        this.rows = h;
        this.board = new Cell[this.cols][this.rows];
        for (int i = 0; i < this.cols; i++)
            for (int j = 0; j < this.rows; j++)
                this.board[i][j] = new Cell();

        Random random = new Random();
        this.colsNums = new ArrayList[this.cols];
        this.rowsNums = new ArrayList[this.rows];
        this.checkedCells = new ArrayList<>();

        this.maxNumbers = 1;
        for (int i = 0; i < this.rows; i++) {
            this.rowsNums[i] = new ArrayList<>();
            this.rowsNums[i].add(-1);
        }

        // creación aleatoria del tablero
        for (int i = 0; i < this.cols; i++) {
            this.colsNums[i] = new ArrayList<>();
            this.colsNums[i].add(-1);
            for (int j = 0; j < this.rows; j++) {
                int rand = random.nextInt(10);
                this.board[i][j].init(rand < this.cellProbability);
                // si es casilla
                if (rand < this.cellProbability) {
                    this.cellsLeft++;

                    // rellenado vector indicador numeros columnas
                    if (this.colsNums[i].get(this.colsNums[i].size() - 1) == -1) {
                        this.colsNums[i].remove(this.colsNums[i].size() - 1);
                        this.colsNums[i].add(1);
                        if (this.maxNumbers < colsNums[i].size())
                            this.maxNumbers = colsNums[i].size();
                    }
                    else
                        this.colsNums[i].set(this.colsNums[i].size() - 1, this.colsNums[i].get(this.colsNums[i].size() - 1) + 1);

                    // rellenado vector indicador numeros filas
                    if (this.rowsNums[j].get(this.rowsNums[j].size() - 1) == -1) {
                        this.rowsNums[j].remove(this.rowsNums[j].size() - 1);
                        this.rowsNums[j].add(1);
                        if (this.maxNumbers < this.rowsNums[j].size())
                            this.maxNumbers = this.rowsNums[j].size();
                    } else
                        this.rowsNums[j].set(this.rowsNums[j].size() - 1, this.rowsNums[j].get(this.rowsNums[j].size() - 1) + 1);
                } else {   //Añadimos -1 al vector de filas y columnas si no es solucion y la casilla anterior si
                    if (this.colsNums[i].get(this.colsNums[i].size() - 1) != -1)
                        this.colsNums[i].add(-1);
                    if (this.rowsNums[j].get(this.rowsNums[j].size() - 1) != -1)
                        this.rowsNums[j].add(-1);
                }
            }

            // -1 at the end of every numbers' column
            if (this.colsNums[i].get(this.colsNums[i].size() - 1) != -1)
                this.colsNums[i].add(-1);
        }

        // -1 at the end of every numbers' row
        for (int i = 0; i < this.rows; i++)
            if (this.rowsNums[i].get(this.rowsNums[i].size() - 1) != -1)
                this.rowsNums[i].add(-1);

        int winW = (9 * GameManager.getInstance().getWidth() / 10) / (cols + 1);
        int winH = (7 * GameManager.getInstance().getHeight() / 10) / (rows + 1);

        this.board_cell_size = Math.min(winH, winW);
        this.separation_margin = Math.max(this.board_cell_size / 25, 1);
        this.board_cell_size -= this.separation_margin;
        this.fontSize = this.board_cell_size / 3;

        this.posX = (GameManager.getInstance().getWidth() - (this.board_cell_size + this.separation_margin) * this.cols
                - this.maxNumbers * this.fontSize) / 2;
        this.posY = ((int) (GameManager.getInstance().getHeight() / 0.75f) - (this.board_cell_size + this.separation_margin) * this.rows
                - this.maxNumbers * this.fontSize) / 2;

        // error message setup
        this.wrongCells = new Label(GameManager.getInstance().getWidth()  / 2, this.posY - GameManager.getInstance().getHeight() / 10,
                "", Resources.FONT_SIMPLY_SQUARE_BIG);
        this.missingCells = new Label(GameManager.getInstance().getWidth() / 2, this.posY - GameManager.getInstance().getHeight() / 18,
                "", Resources.FONT_SIMPLY_SQUARE_BIG);
        this.lastTimeChecked = -1;
    }

    public void render(IRender renderer) {
        // board limits
        renderer.setColor(0xFF000000);
        renderer.drawRectangle(this.maxNumbers * this.fontSize + this.posX, this.posY + this.maxNumbers * this.fontSize,
                this.cols * (this.board_cell_size + this.separation_margin) + 1,
                this.rows * (this.board_cell_size + this.separation_margin) + 1, false);
        renderer.drawRectangle(this.posX + this.maxNumbers * this.fontSize, this.maxNumbers * this.fontSize + this.posY,
                this.cols * (this.board_cell_size + this.separation_margin) + 1,
                this.rows * (this.board_cell_size + this.separation_margin) + 1, false);

        // board number indicators
        printNumbers(renderer);

        // board cells
        for (int i = 0; i < this.cols; i++)
            for (int j = 0; j < this.rows; j++)
                this.board[i][j].render(renderer, i * this.board_cell_size + (i + 1) * this.separation_margin +
                                this.posX + this.maxNumbers * this.fontSize,
                        j * this.board_cell_size + (j + 1) * this.separation_margin +
                                this.posY + this.maxNumbers * this.fontSize, this.board_cell_size);

        // board fail text
        if (this.lastTimeChecked != -1) {
            this.wrongCells.setColor(0xFFFF0000);
            this.missingCells.setColor(0xFFFF0000);

            this.wrongCells.setText("Te faltan " + this.cellsLeft + " casillas");
            this.missingCells.setText("Tienes mal " + this.checkedCells.size() + " casillas");

            this.wrongCells.render(renderer);
            this.missingCells.render(renderer);
        }
    }

    public void renderWin(IRender renderer) {
        this.posX = (GameManager.getInstance().getWidth() - this.board_cell_size * cols - this.separation_margin * (this.cols + 1)) / 2;
        for (int i = 0; i < this.cols; i++)
            for (int j = 0; j < this.rows; j++)
                if (this.board[i][j].isAnswer())
                    this.board[i][j].render(renderer, i * this.board_cell_size + (i + 1) * this.separation_margin + this.posX,
                            j * this.board_cell_size + (j + 1) * this.separation_margin + this.posY - GameManager.getInstance().getHeight() / 10,
                            this.board_cell_size);
    }

    public void update(double deltaTime) {
        // check timer
        if (this.lastTimeChecked != -1) {
            if (this.lastTimeChecked - deltaTime < 0) {
                this.lastTimeChecked = -1;
                for (int i = 0; i < this.checkedCells.size(); i++)
                    this.checkedCells.get(i).uncheck();
                this.checkedCells.clear();
            } else
                this.lastTimeChecked -= deltaTime;
        }
    }

    private void printNumbers(IRender renderer) {
        renderer.setFont(renderer.loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, this.fontSize));
        for (int i = 0; i < this.colsNums.length; i++) {
            if (this.colsNums[i].size() == 1)
                renderer.drawText(this.posX + this.board_cell_size * (i + 1) + this.separation_margin * i -
                                this.board_cell_size / 2 + this.maxNumbers * fontSize,
                        this.posY + this.maxNumbers * this.fontSize - 2 * this.separation_margin, "0");
            for (int j = this.colsNums[i].size() - 2; j >= 0; j--) {
                int w = this.colsNums[i].get(j);
                renderer.drawText(this.posX + this.board_cell_size * (i + 1) + this.separation_margin * i -
                                this.board_cell_size / 2 + this.maxNumbers * this.fontSize,
                        this.posY + this.maxNumbers * this.fontSize - 2 * this.separation_margin -
                                (this.fontSize * (this.colsNums[i].size() - 2 - j)),
                        Integer.toString(w));
            }
        }

        for (int i = 0; i < this.rowsNums.length; i++) {
            if (this.rowsNums[i].size() == 1)
                renderer.drawText(this.posX + this.maxNumbers * this.fontSize - 8 * this.separation_margin,
                        this.posY + this.board_cell_size * (i + 1) + this.separation_margin * i -
                                (int) (this.board_cell_size / 2.5) + this.maxNumbers * this.fontSize, "0");
            for (int j = rowsNums[i].size() - 2; j >= 0; j--) {
                int w = rowsNums[i].get(j);
                renderer.drawText(this.posX + this.maxNumbers * this.fontSize - 8 * this.separation_margin -
                                (this.fontSize * (this.rowsNums[i].size() - 2 - j)),
                        this.posY + this.board_cell_size * (i + 1) + this.separation_margin * i -
                                (int) (this.board_cell_size / 2.5) + this.maxNumbers * this.fontSize, Integer.toString(w));
            }
        }
    }

    public void check() {
        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                if (!this.board[i][j].isAnswer() && this.board[i][j].getState() == Cell.State.MARKED) {
                    this.board[i][j].setChecked();
                    this.checkedCells.add(this.board[i][j]);
                }
            }
        }

        this.lastTimeChecked = SECONDS_CHECKED;
        if (this.checkedCells.size() == 0 && this.cellsLeft == 0)
            this.win = true;
    }

    public boolean isInBoard(int posX, int posY) {
        return posX > (this.separation_margin + this.posX + this.maxNumbers * this.fontSize) &&
                posX < (this.cols * this.board_cell_size + this.cols * this.separation_margin + this.posX + this.maxNumbers * this.fontSize) &&
                posY > (this.separation_margin + this.posY + this.maxNumbers * this.fontSize) &&
                posY < (this.rows * this.board_cell_size + this.rows * this.separation_margin + this.posY + this.maxNumbers * this.fontSize);
    }

    public void markCell(int x, int y) {
        int boardX = (x - this.posX - this.separation_margin - this.maxNumbers * this.fontSize) -
                (x - this.posX - this.separation_margin - this.maxNumbers * this.fontSize) / this.board_cell_size * this.separation_margin;
        int boardY = (y - this.posY - this.separation_margin - this.maxNumbers * this.fontSize) -
                (y - this.posY - this.separation_margin - this.maxNumbers * this.fontSize) / this.board_cell_size * this.separation_margin;
        this.cellsLeft -= this.board[boardX / this.board_cell_size][boardY / this.board_cell_size].changeCell();
    }

    public int getCellSize() {
        return this.board_cell_size;
    }

    public int getMarginCells() {
        return this.separation_margin;
    }

    public int getCols() {
        return this.colsNums.length;
    }

    public int getRows() {
        return this.rowsNums.length;
    }
}

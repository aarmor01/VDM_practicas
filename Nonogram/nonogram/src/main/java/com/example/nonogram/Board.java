package com.example.nonogram;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.shared.FontType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    static int SEGS_CHECKED = 5;

    private int board_cell_size;
    private int separation_margin;
    private int fontSize;
    private Cell[][] board;
    private List<Integer>[] cols;
    private List<Integer>[] rows;
    private int cellsLeft;
    private int height, width;
    private int posX = 0, posY = 0;
    private String font;
    private String fontButtons;
    private String sound;
    private String music;

    private int maxNumbers;
    private List<Cell> checkedCells;
    private double lastTimeChecked;

    public void render(IRender renderMng) {
        renderMng.setColor(0xFF000000); //Cuadrados alrededor
        renderMng.drawRectangle(maxNumbers * fontSize + posX, posY + maxNumbers * fontSize, width * (board_cell_size + separation_margin) + 1,
                height * (board_cell_size + separation_margin) + 1, false);
        renderMng.drawRectangle(posX + maxNumbers * fontSize, maxNumbers * fontSize + posY, width * (board_cell_size + separation_margin) + 1,
                height * (board_cell_size + separation_margin) + 1, false);

        printNumbers(renderMng);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j].render(renderMng, i * board_cell_size + (i + 1) * separation_margin + posX + maxNumbers * fontSize,
                        j * board_cell_size + (j + 1) * separation_margin + posY + maxNumbers * fontSize, board_cell_size);
            }
        }

        if (lastTimeChecked != -1) {
            renderMng.setColor(0xFFFF0000);
            int x = renderMng.getTextWidth(font, "Te faltan " + checkedCells.size() + " casillas");
            int x2 = renderMng.getTextWidth(font, "Tienes mal " + checkedCells.size() + " casillas");
            renderMng.drawText((renderMng.getWidth() - x) / 2, posY - renderMng.getHeight() / 10, "Te faltan " + cellsLeft + " casillas");
            renderMng.drawText((renderMng.getWidth() - x2) / 2, posY - renderMng.getHeight() / 10 + fontSize * 2, "Tienes mal " + checkedCells.size() + " casillas");
        }
    }

    public void init(int h, int w, IEngine eng) {
        height = h;
        width = w;
        board = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = new Cell();
            }
        }

        cols = new ArrayList[width];
        rows = new ArrayList[height];
        Random random = new Random();

        checkedCells = new ArrayList<Cell>();

        maxNumbers = 1;
        for (int i = 0; i < height; i++) {
            rows[i] = new ArrayList<Integer>();
            rows[i].add(-1);
            if (maxNumbers < rows[i].size())
                maxNumbers = rows[i].size();
        }

        for (int i = 0; i < width; i++) {        //Creación aleatoria del tablero
            cols[i] = new ArrayList<Integer>();
            cols[i].add(-1);
            for (int j = 0; j < height; j++) {
                int rand = random.nextInt(10);
                board[i][j].init(rand < 4);
                if (rand < 4) {
                    cellsLeft++;

                    if (cols[i].get(cols[i].size() - 1) == -1) {      //Rellenado vector columnas
                        cols[i].remove(cols[i].size() - 1);
                        cols[i].add(1);
                        if (maxNumbers < cols[i].size())
                            maxNumbers = cols[i].size();
                    } else {
                        cols[i].set(cols[i].size() - 1, cols[i].get(cols[i].size() - 1) + 1);
                    }

                    if (rows[j].get(rows[j].size() - 1) == -1) {      //Rellenado vector filas
                        rows[j].remove(rows[j].size() - 1);
                        rows[j].add(1);
                        if (maxNumbers < rows[j].size())
                            maxNumbers = rows[j].size();
                    } else {
                        rows[j].set(rows[j].size() - 1, rows[j].get(rows[j].size() - 1) + 1);
                    }
                } else {   //Añadimos -1 al vector de filas y columnas si no es solucion y la casilla anterior si
                    if (cols[i].get(cols[i].size() - 1) != -1)
                        cols[i].add(-1);
                    if (rows[j].get(rows[j].size() - 1) != -1)
                        rows[j].add(-1);
                }
            }
            if (cols[i].get(cols[i].size() - 1) != -1)
                cols[i].add(-1);
        }

        for (int i = 0; i < height; i++) {
            if (rows[i].get(rows[i].size() - 1) != -1)
                rows[i].add(-1);
        }
        int maxDimension = Math.max(w, h);

        int winW = (eng.getRender().getWidth()) / (maxDimension + maxDimension / 8);
        int winH = ((int)(eng.getRender().getHeight() / 1.85) - maxNumbers * fontSize) / (maxDimension + maxDimension / 8);

        board_cell_size = Math.min(winH, winW);
        separation_margin = Math.max(board_cell_size / 25, 1);
        board_cell_size -= separation_margin;
        fontSize = board_cell_size / 3;

        posX = (eng.getRender().getWidth() - (board_cell_size + separation_margin) * width - maxNumbers * fontSize) / 2;
        posY = ((int) (eng.getRender().getHeight() / 0.75f) - (board_cell_size + separation_margin) * height - maxNumbers * fontSize) / 2;

        font = eng.getRender().loadFont("./assets/fonts/SimplySquare.ttf", FontType.DEFAULT, fontSize);
        lastTimeChecked = -1;
    }

    public void update(double deltaTime) {
        if (lastTimeChecked != -1) {
            if (lastTimeChecked - deltaTime < 0) {
                lastTimeChecked = -1;
                for (int i = 0; i < checkedCells.size(); i++) {
                    checkedCells.get(i).unChecked();
                }
                checkedCells.clear();
            } else
                lastTimeChecked -= deltaTime;
        }
    }

    private void printNumbers(IRender renderMng) {
        renderMng.setFont(font);
        for (int i = 0; i < cols.length; i++) {
            if (cols[i].size() == 1) {
                renderMng.drawText(posX + board_cell_size * (i + 1) + separation_margin * i - board_cell_size / 2 + maxNumbers * fontSize,
                        posY + (int) (maxNumbers * (fontSize / 2) / 0.5), "0");
            }
            for (int j = cols[i].size() - 2; j >= 0; j--) {
                int w = cols[i].get(j);
                renderMng.drawText(posX + board_cell_size * (i + 1) + separation_margin * i - board_cell_size / 2 + maxNumbers * fontSize,
                        posY + (int) (maxNumbers * (fontSize / 2) / 0.5) - (fontSize * (cols[i].size() - 2 - j)), Integer.toString(w));
            }
        }

        for (int i = 0; i < rows.length; i++) {
            if (rows[i].size() == 1) {
                renderMng.drawText(posX + (int) (maxNumbers * (fontSize / 2) / 0.7),
                        posY + board_cell_size * (i + 1) + separation_margin * i - (int) (board_cell_size / 2.5) + maxNumbers * fontSize, "0");
            }
            for (int j = rows[i].size() - 2; j >= 0; j--) {
                int w = rows[i].get(j);
                renderMng.drawText(posX + (int) (maxNumbers * (fontSize / 2) / 0.7) - (fontSize * (rows[i].size() - 2 - j)),
                        posY + board_cell_size * (i + 1) + separation_margin * i - (int) (board_cell_size / 2.5) + maxNumbers * fontSize, Integer.toString(w));
            }
        }
    }

    public void checkear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!board[i][j].isAnswer() && board[i][j].getState() == Cell.State.MARKED) {
                    board[i][j].setChecked();
                    checkedCells.add(board[i][j]);
                }
            }
        }
        lastTimeChecked = SEGS_CHECKED;
    }

    public boolean isInBoard(int posX, int posY) {
        return posX > (separation_margin + this.posX + maxNumbers * fontSize) && posX < (width * board_cell_size + width * separation_margin + this.posX + maxNumbers * fontSize)
                && posY > (separation_margin + this.posY + maxNumbers * fontSize) && posY < (height * board_cell_size + height * separation_margin + this.posY + maxNumbers * fontSize);
    }

    public void markCell(int x, int y) {
        int boardX = (x - posX - separation_margin - maxNumbers * fontSize) - (x - posX - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin;
        int boardY = (y - posY - separation_margin - maxNumbers * fontSize) - (y - posY - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin;
        cellsLeft -= board[boardX / board_cell_size][boardY / board_cell_size].changeCell();
    }

    public int getCellSize() {
        return board_cell_size;
    }

    public int getMarginCells() {
        return separation_margin;
    }

    public int getWidth() {
        return cols.length;
    }

    public int getHeight() {
        return rows.length;
    }

    public String getFont() {
        return font;
    }
}

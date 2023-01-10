package com.example.app_android.Objects;

import com.example.app_android.GameManager;
import com.example.app_android.Resources;
import com.example.engine_android.EngineAndroid;
import com.example.engine_android.Enums.FontType;
import com.example.engine_android.Modules.RenderAndroid;

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
    private String font;
    private String fontWrongText;

    private List<Cell> checkedCells;
    private double lastTimeChecked;


    public void init(int h, int w, EngineAndroid eng, ArrayList<String> content) {
        rows = h;
        cols = w;
        board = new Cell[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                board[i][j] = new Cell();
            }
        }

        colsNums = new ArrayList[cols];
        rowsNums = new ArrayList[rows];
        Random random = new Random();

        checkedCells = new ArrayList<Cell>();

        maxNumbers = 1;
        for (int i = 0; i < rows; i++) {
            rowsNums[i] = new ArrayList<Integer>();
            rowsNums[i].add(-1);
            if (maxNumbers < rowsNums[i].size())
                maxNumbers = rowsNums[i].size();
        }

        for (int i = 0; i < cols; i++) {        //Creación aleatoria del tablero
            colsNums[i] = new ArrayList<Integer>();
            colsNums[i].add(-1);
            for (int j = 0; j < rows; j++) {
                if(content == null){        //Diferenciamos entre lectura de ficharo y aleatorio
                    int rand = random.nextInt(10);
                    board[i][j].init(rand < 4, Cell.State.EMPTY);
                }
                else{
                    board[i][j].init(content.get(j+1).charAt(i) == '1', Cell.State.EMPTY);
                }

                if (board[i][j].isAnswer()) {   //Si la casilla es solucion modificamos los arrays de filas-columnas
                    cellsLeft++;

                    if (colsNums[i].get(colsNums[i].size() - 1) == -1) {      //Rellenado vector columnas
                        colsNums[i].remove(colsNums[i].size() - 1);
                        colsNums[i].add(1);
                        if (maxNumbers < colsNums[i].size())
                            maxNumbers = colsNums[i].size();
                    }
                    else {
                        colsNums[i].set(colsNums[i].size() - 1, colsNums[i].get(colsNums[i].size() - 1) + 1);
                    }

                    if (rowsNums[j].get(rowsNums[j].size() - 1) == -1) {      //Rellenado vector filas
                        rowsNums[j].remove(rowsNums[j].size() - 1);
                        rowsNums[j].add(1);
                        if (maxNumbers < rowsNums[j].size())
                            maxNumbers = rowsNums[j].size();
                    }
                    else {
                        rowsNums[j].set(rowsNums[j].size() - 1, rowsNums[j].get(rowsNums[j].size() - 1) + 1);
                    }
                }
                else {   //Añadimos -1 al vector de filas y columnas si no es solucion y la casilla anterior si
                    if (colsNums[i].get(colsNums[i].size() - 1) != -1)
                        colsNums[i].add(-1);
                    if (rowsNums[j].get(rowsNums[j].size() - 1) != -1)
                        rowsNums[j].add(-1);
                }
            }
            if (colsNums[i].get(colsNums[i].size() - 1) != -1)
                colsNums[i].add(-1);
        }

        for (int i = 0; i < rows; i++) {
            if (rowsNums[i].get(rowsNums[i].size() - 1) != -1)
                rowsNums[i].add(-1);
        }

        calcCellSize(eng);

        lastTimeChecked = -1;
    }

    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000); //Cuadrado alrededor
        renderMng.drawRectangle(maxNumbers * fontSize + posX, posY + maxNumbers * fontSize, cols * (board_cell_size + separation_margin) + separation_margin,
                rows * (board_cell_size + separation_margin) + separation_margin, false);

        printNumbers(renderMng);    //Escribimos los números

        for (int i = 0; i < cols; i++) {   //Dibujado casillas
            for (int j = 0; j < rows; j++) {
                board[i][j].render(renderMng, i * board_cell_size + (i + 1) * separation_margin + posX + maxNumbers * fontSize,
                        j * board_cell_size + (j + 1) * separation_margin + posY + maxNumbers * fontSize, board_cell_size);
            }
        }

        renderMng.setFont(fontWrongText);

        if (lastTimeChecked != -1) {    //Texto de comprobacion
            renderMng.setColor(0xFFFF0000);
            int x = renderMng.getTextWidth(fontWrongText, "Te faltan " + checkedCells.size() + " casillas");
            int x2 = renderMng.getTextWidth(fontWrongText, "Tienes mal " + checkedCells.size() + " casillas");
            int y = renderMng.getTextHeight(fontWrongText);
            renderMng.drawText((renderMng.getWidth() - x) / 2, posY - renderMng.getHeight() / 10, "Te faltan " + cellsLeft + " casillas");
            renderMng.drawText((renderMng.getWidth() - x2) / 2, posY - renderMng.getHeight() / 10 + y * 2, "Tienes mal " + checkedCells.size() + " casillas");
        }
    }

    public void renderWin(RenderAndroid renderMng) {    //Dibujamos solo las casillas solucion
        posX = (GameManager.getInstance().getWidth() - board_cell_size * cols - separation_margin * (cols + 1)) / 2;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (board[i][j].isAnswer()) {
                    board[i][j].render(renderMng, i * board_cell_size + (i + 1) * separation_margin + posX,
                            j * board_cell_size + (j + 1) * separation_margin + posY - GameManager.getInstance().getHeight() / 10, board_cell_size);
                }
            }
        }
    }

    public void initFile(ArrayList<String> level, EngineAndroid eng){
        String[] lines = level.get(0).split(" ");
        this.init(Integer.valueOf(lines[1]), Integer.valueOf(lines[0]), eng, level);
    }

    public void update(double deltaTime) {
        if (lastTimeChecked != -1) {
            if (lastTimeChecked - deltaTime < 0) {
                lastTimeChecked = -1;
                for (int i = 0; i < checkedCells.size(); i++) {
                    checkedCells.get(i).unChecked();
                    checkedCells.get(i).markCell();
                }
                checkedCells.clear();
            } else
                lastTimeChecked -= deltaTime;
        }
    }

    private void printNumbers(RenderAndroid renderMng) {
        renderMng.setFont(font);
        for (int i = 0; i < colsNums.length; i++) {
            if (colsNums[i].size() == 1) {
                renderMng.drawText(posX + board_cell_size * (i + 1) + separation_margin * i - board_cell_size / 2 + maxNumbers * fontSize,
                        posY + maxNumbers * fontSize - 2 * separation_margin, "0");
            }
            for (int j = colsNums[i].size() - 2; j >= 0; j--) {
                int w = colsNums[i].get(j);
                renderMng.drawText(posX + board_cell_size * (i + 1) + separation_margin * i - board_cell_size / 2 + maxNumbers * fontSize,
                        posY + maxNumbers * fontSize - 2 * separation_margin - (fontSize * (colsNums[i].size() - 2 - j)), Integer.toString(w));
            }
        }

        for (int i = 0; i < rowsNums.length; i++) {
            if (rowsNums[i].size() == 1) {
                renderMng.drawText(posX + maxNumbers * fontSize - 8 * separation_margin,
                        posY + board_cell_size * (i + 1) + separation_margin * i - (int) (board_cell_size / 2.5) + maxNumbers * fontSize, "0");
            }
            for (int j = rowsNums[i].size() - 2; j >= 0; j--) {
                int w = rowsNums[i].get(j);
                renderMng.drawText(posX + maxNumbers * fontSize - 8 * separation_margin - (fontSize * (rowsNums[i].size() - 2 - j)),
                        posY + board_cell_size * (i + 1) + separation_margin * i - (int) (board_cell_size / 2.5) + maxNumbers * fontSize, Integer.toString(w));
            }
        }
    }

    public boolean checkear(int x, int y) {
        int boardX = ((x - posX - separation_margin - maxNumbers * fontSize) - (x - posX - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;
        int boardY = ((y - posY - separation_margin - maxNumbers * fontSize) - (y - posY - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;

        if (!board[boardX][boardY].isAnswer() && board[boardX][boardY].getState() == Cell.State.MARKED) {
            board[boardX][boardY].setChecked();
            checkedCells.add(board[boardX][boardY]);
            lastTimeChecked = SECONDS_CHECKED;
        }

        if (cellsLeft == 0)
            win = true;

        return win;
    }

    public boolean isInBoard(int posX, int posY) {
        return posX > (separation_margin + this.posX + maxNumbers * fontSize) && posX < (cols * board_cell_size + cols * separation_margin + this.posX + maxNumbers * fontSize)
                && posY > (separation_margin + this.posY + maxNumbers * fontSize) && posY < (rows * board_cell_size + rows * separation_margin + this.posY + maxNumbers * fontSize);
    }

    public int markCell(int x, int y, boolean longT) {
        int boardX = ((x - posX - separation_margin - maxNumbers * fontSize) - (x - posX - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;
        int boardY = ((y - posY - separation_margin - maxNumbers * fontSize) - (y - posY - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;
        if (longT)
            board[boardX][boardY].crossCell();
        else {
            board[boardX][boardY].markCell();
            if (board[boardX][boardY].getState() == Cell.State.MARKED) {
                if (board[boardX][boardY].isAnswer())
                    cellsLeft -= 1;
                else
                    return 1;
            } else {
                if (board[boardX][boardY].isAnswer())
                    cellsLeft += 1;
            }
        }

        return 0;
    }

    public int getCols() {
        return colsNums.length;
    }

    public int getRows() {
        return rowsNums.length;
    }

    public int[][] getBoardState() {
        int[][] cellState = new int[board.length][board[0].length];

        for (int row = 0; row < board.length; row++)
            for (int col = 0; col < board[0].length; col++) // we ignore the marked state
                cellState[row][col] = board[row][col].getState().ordinal() % 3;

        return cellState;
    }

    public int setBoardState(int[][] savedState) {
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[0].length; col++){
                Cell.State cellState = Cell.State.values()[savedState[row][col]];
                board[row][col].setState(cellState);

                if(cellState == Cell.State.CHECKED)     // if checked, it was an error
                    checkedCells.add(board[row][col]);
                else if(cellState == Cell.State.MARKED && board[row][col].isAnswer())   // if marked and answer, discount from cellsleft
                    cellsLeft--;
            }
        }

        return 0;
    }

    public void setPos(int x, int y){
        posX = x;
        posY = y;
    }

    public int getHeightInPixels(){ return (board_cell_size + separation_margin) * rows + maxNumbers * fontSize; }

    public int getWidthInPixels(){ return (board_cell_size + separation_margin) * cols + maxNumbers * fontSize; }

    public void calcCellSize(EngineAndroid eng){
        int winW = (9 * GameManager.getInstance().getWidth() / 10 - maxNumbers * fontSize) / (cols);
        int winH = (7 * GameManager.getInstance().getHeight() / 10 - maxNumbers * fontSize) / (rows);

        board_cell_size = Math.min(winH, winW);
        separation_margin = Math.max(board_cell_size / 25, 1);
        board_cell_size -= separation_margin;
        fontSize = board_cell_size / 3;

        font = eng.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, fontSize);
        fontWrongText = Resources.FONT_SIMPLY_SQUARE_BIG;
    }
}

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
    static double SECONDS_CHECKED = 0.5;

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
    private String fontWrongText;

    private int maxNumbers;
    private List<Cell> checkedCells;
    private double lastTimeChecked;

    public boolean win = false;

    public void init(int h, int w, EngineAndroid eng, ArrayList<String> content) {
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
                if(content == null){        //Diferenciamos entre lectura de ficharo y aleatorio
                    int rand = random.nextInt(10);
                    board[i][j].init(rand < 4, Cell.State.EMPTY);
                }
                else{
                    board[i][j].init(content.get(i+1).charAt(j) == '1', Cell.State.EMPTY);
                }

                if (board[i][j].isAnswer()) {   //Si la casilla es solucion modificamos los arrays de filas-columnas
                    cellsLeft++;

                    if (cols[i].get(cols[i].size() - 1) == -1) {      //Rellenado vector columnas
                        cols[i].remove(cols[i].size() - 1);
                        cols[i].add(1);
                        if (maxNumbers < cols[i].size())
                            maxNumbers = cols[i].size();
                    }
                    else {
                        cols[i].set(cols[i].size() - 1, cols[i].get(cols[i].size() - 1) + 1);
                    }

                    if (rows[j].get(rows[j].size() - 1) == -1) {      //Rellenado vector filas
                        rows[j].remove(rows[j].size() - 1);
                        rows[j].add(1);
                        if (maxNumbers < rows[j].size())
                            maxNumbers = rows[j].size();
                    }
                    else {
                        rows[j].set(rows[j].size() - 1, rows[j].get(rows[j].size() - 1) + 1);
                    }
                }
                else {   //Añadimos -1 al vector de filas y columnas si no es solucion y la casilla anterior si
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

        calcCellSize(eng);

        lastTimeChecked = -1;
    }

    public void render(RenderAndroid renderMng) {
        renderMng.setColor(0xFF000000); //Cuadrado alrededor
        renderMng.drawRectangle(maxNumbers * fontSize + posX, posY + maxNumbers * fontSize, height * (board_cell_size + separation_margin) + separation_margin,
                width * (board_cell_size + separation_margin) + separation_margin, false);

        printNumbers(renderMng);    //Escribimos los números

        for (int i = 0; i < height; i++) {   //Dibujado casillas
            for (int j = 0; j < width; j++) {
                board[j][i].render(renderMng, i * board_cell_size + (i + 1) * separation_margin + posX + maxNumbers * fontSize,
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
        posX = (GameManager.getInstance().getWidth() - board_cell_size * height - separation_margin * (height + 1)) / 2;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board[j][i].isAnswer()) {
                    board[j][i].render(renderMng, i * board_cell_size + (i + 1) * separation_margin + posX,
                            j * board_cell_size + (j + 1) * separation_margin + posY - GameManager.getInstance().getHeight() / 10, board_cell_size);
                }
            }
        }
    }

    public void initFile(ArrayList<String> level, EngineAndroid eng){
        String[] lines = level.get(0).split(" ");
        this.init(Integer.valueOf(lines[0]), Integer.valueOf(lines[1]), eng, level);
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
        for (int i = 0; i < rows.length; i++) {
            if (rows[i].size() == 1) {
                renderMng.drawText(posX + board_cell_size * (i + 1) + separation_margin * i - board_cell_size / 2 + maxNumbers * fontSize,
                        posY + maxNumbers * fontSize - 2 * separation_margin, "0");
            }
            for (int j = rows[i].size() - 2; j >= 0; j--) {
                int w = rows[i].get(j);
                renderMng.drawText(posX + board_cell_size * (i + 1) + separation_margin * i - board_cell_size / 2 + maxNumbers * fontSize,
                        posY + maxNumbers * fontSize - 2 * separation_margin - (fontSize * (rows[i].size() - 2 - j)), Integer.toString(w));
            }
        }

        for (int i = 0; i < cols.length; i++) {
            if (cols[i].size() == 1) {
                renderMng.drawText(posX + maxNumbers * fontSize - 8 * separation_margin,
                        posY + board_cell_size * (i + 1) + separation_margin * i - (int) (board_cell_size / 2.5) + maxNumbers * fontSize, "0");
            }
            for (int j = cols[i].size() - 2; j >= 0; j--) {
                int w = cols[i].get(j);
                renderMng.drawText(posX + maxNumbers * fontSize - 8 * separation_margin - (fontSize * (cols[i].size() - 2 - j)),
                        posY + board_cell_size * (i + 1) + separation_margin * i - (int) (board_cell_size / 2.5) + maxNumbers * fontSize, Integer.toString(w));
            }
        }
    }

    public boolean checkear(int x, int y) {
        int boardY = ((x - posX - separation_margin - maxNumbers * fontSize) - (x - posX - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;
        int boardX = ((y - posY - separation_margin - maxNumbers * fontSize) - (y - posY - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;

        if (!board[boardX][boardY].isAnswer() && board[boardX][boardY].getState() == Cell.State.MARKED) {
            board[boardX][boardY].setChecked();
            checkedCells.add(board[boardX][boardY]);
        }

        lastTimeChecked = SECONDS_CHECKED;
        if (cellsLeft == 0)
            win = true;

        return win;
    }

    public boolean isInBoard(int posX, int posY) {
        return posX > (separation_margin + this.posX + maxNumbers * fontSize) && posX < (height * board_cell_size + height * separation_margin + this.posX + maxNumbers * fontSize)
                && posY > (separation_margin + this.posY + maxNumbers * fontSize) && posY < (width * board_cell_size + width * separation_margin + this.posY + maxNumbers * fontSize);
    }

    public int markCell(int x, int y, boolean longT) {
        int boardY = ((x - posX - separation_margin - maxNumbers * fontSize) - (x - posX - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;
        int boardX = ((y - posY - separation_margin - maxNumbers * fontSize) - (y - posY - separation_margin - maxNumbers * fontSize) / board_cell_size * separation_margin) / board_cell_size;
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

    public int getWidth() {
        return cols.length;
    }

    public int getHeight() {
        return rows.length;
    }

    public int[][] getBoardState() {
        int[][] cellState = new int[board.length][board[0].length];

        for (int row = 0; row < board.length; row++)
            for (int col = 0; col < board[0].length; col++) // we ignore the marked state
                cellState[row][col] = board[col][row].getState().ordinal() % 3;

        return cellState;
    }

    public int setBoardState(int[][] savedState) {
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[0].length; col++){
                Cell.State cellState = Cell.State.values()[savedState[row][col]];
                board[col][row].setState(cellState);

                if(cellState == Cell.State.CHECKED)     // if checked, it was an error
                    checkedCells.add(board[col][row]);
                else if(cellState == Cell.State.MARKED && board[col][row].isAnswer())   // if marked and answer, discount from cellsleft
                    cellsLeft--;
            }
        }

        return 0;
    }

    public void setPos(int x, int y){
        posX = x;
        posY = y;
    }

    public int getHeightInPixels(){ return (board_cell_size + separation_margin) * width + maxNumbers * fontSize; }

    public int getWidthInPixels(){ return (board_cell_size + separation_margin) * height + maxNumbers * fontSize; }

    public void calcCellSize(EngineAndroid eng){
        int maxDimension = Math.max(width, height);

        int winW = (9 * GameManager.getInstance().getWidth() / 10 - maxNumbers * fontSize) / (maxDimension);
        int winH = (9 * GameManager.getInstance().getHeight() / 10 - maxNumbers * fontSize) / (maxDimension);

        board_cell_size = Math.min(winH, winW);
        separation_margin = Math.max(board_cell_size / 25, 1);
        board_cell_size -= separation_margin;
        fontSize = board_cell_size / 3;

        font = eng.getRender().loadFont("fonts/SimplySquare.ttf", FontType.DEFAULT, fontSize);
        fontWrongText = Resources.FONT_SIMPLY_SQUARE_BIG;
    }
}
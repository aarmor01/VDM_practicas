package com.example.nonogram;

import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.shared.FontType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private Cell[][] board;
    private List<Integer>[] cols;
    private List<Integer>[] rows;
    private int cellsLeft;
    private int height, width;
    private String font;

    public void render(IRender renderMng){
        int x=30, y=50;
        renderMng.setColor(0xFF000000);
        renderMng.drawRectangle(50 + x, y,width*52, (height + 1)*52, false);
        renderMng.drawRectangle(x,50 + y,(width + 1)*52, height*52, false);

        printNumbers(renderMng);

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                board[i][j].render(renderMng, (i + 1)*50 + (i+1)*2 + x, (j + 1)*50 + (j + 1)*2 + y, 50);
            }
        }
    }

    public void init(int h, int w, IEngine eng) {
        height = h;
        width = w;
        board = new Cell[width][height];
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                board[i][j] = new Cell();
            }
        }
        cols = new ArrayList[width];
        rows = new ArrayList[height];
        Random random = new Random();

        for(int i=0; i<height; i++) {
            rows[i] = new ArrayList<Integer>();
            rows[i].add(-1);
        }

        for(int i = 0; i < width; i++){        //Creación aleatoria del tablero
            cols[i] = new ArrayList<Integer>();
            cols[i].add(-1);
            for(int j = 0; j < height; j++){
                int rand = random.nextInt(10);
                board[i][j].init(rand<4);
                if (rand < 4) {
                    cellsLeft++;

                    if(cols[i].get(cols[i].size() - 1) == -1){      //Rellenado vector columnas
                        cols[i].remove(cols[i].size()-1);
                        cols[i].add(1);
                    }
                    else{
                        cols[i].set(cols[i].size() - 1, cols[i].get(cols[i].size() - 1) + 1);
                    }

                    if(rows[j].get(rows[j].size() - 1) == -1){      //Rellenado vector filas
                        rows[j].remove(rows[j].size()-1);
                        rows[j].add(1);
                    }
                    else{
                        rows[j].set(rows[j].size() - 1, rows[j].get(rows[j].size() - 1) + 1);
                    }
                }
                else{   //Añadimos -1 al vector de filas y columnas si no es solucion y la casilla anterior si
                    if(cols[i].get(cols[i].size() - 1) != -1)
                        cols[i].add(-1);
                    if(rows[j].get(rows[j].size() - 1) != -1)
                        rows[j].add(-1);
                }
            }
            if(cols[i].get(cols[i].size() - 1) != -1)
                cols[i].add(-1);
        }

        for(int i=0; i<height; i++) {
            if (rows[i].get(rows[i].size() - 1) != -1)
                rows[i].add(-1);
        }


        font = eng.getRender().loadFont("Assets/Fonts/arial.ttf", FontType.DEFAULT, 20);
    }

    private void printNumbers(IRender renderMng){
        renderMng.setFont(font);
        for(int i=0; i< cols.length; i++){
            if(cols[i].size() == 1){
                renderMng.drawText(50 * (i + 1) + 30 + 20, 90, "0");
            }
            for(int j=cols[i].size()-2; j>=0; j--) {
                int w = cols[i].get(j);
                renderMng.drawText(50 * (i + 1) + 30 + 20, 90 - (18*(cols[i].size()-2-j)), Integer.toString(w));
            }
        }

        for(int i=0; i< rows.length; i++){
            if(rows[i].size() == 1){
                renderMng.drawText(40, 50 * (i + 1) + 30 + 50, "0");
            }
            for(int j=rows[i].size()-2; j>=0; j--) {
                int w = rows[i].get(j);
                renderMng.drawText(40 - (18*(cols[i].size()-2-j)),50 * (i + 1) + 30 + 50, Integer.toString(w));
            }
        }
    }

    public void markCell(int x, int y) {
        cellsLeft += board[x][y].changeCell();
    }
}

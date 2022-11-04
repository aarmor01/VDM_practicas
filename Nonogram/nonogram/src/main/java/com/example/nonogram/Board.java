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
        renderMng.setFont(font);
        renderMng.drawText(50+x, y, Integer.toString(cols[0].get(0)));

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


        font = eng.getRender().loadFont("Assets/Fonts/FFF_Tusj.ttf", FontType.BOLD, 20);
    }

    public void markCell(int x, int y) {
        cellsLeft += board[x][y].changeCell();
    }
}

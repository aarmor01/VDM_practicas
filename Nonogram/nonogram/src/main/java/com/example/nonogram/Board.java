package com.example.nonogram;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private Cell[][] board;
    private List<Integer>[] cols;
    private List<Integer>[] rows;
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
        cols = new ArrayList[width];
        rows = new ArrayList[height];
        Random random = new Random();

        for(int i=0; i<width; i++) {
            cols[i] = new ArrayList<Integer>();
            cols[i].add(-1);
        }

        for(int i = 0; i < height; i++){        //Creación aleatoria del tablero
            rows[i] = new ArrayList<Integer>();
            rows[i].add(-1);
            for(int j = 0; j < width; j++){
                int rand = random.nextInt(10);

                board[i][j].init(rand<4);
                if (rand < 4) {
                    cellsLeft++;

                    if(cols[j].get(cols[j].size() - 1) == -1){      //Rellenado vector columnas
                        cols[j].remove(cols[j].size()-1);
                        cols[j].add(1);
                    }
                    else{
                        cols[j].set(cols[j].size() - 1, cols[j].get(cols[j].size() - 1) + 1);
                    }

                    if(rows[i].get(rows[i].size() - 1) == -1){      //Rellenado vector filas
                        rows[i].remove(rows[i].size()-1);
                        rows[i].add(1);
                    }
                    else{
                        rows[i].set(rows[i].size() - 1, rows[i].get(rows[i].size() - 1) + 1);
                    }
                }
                else{   //Añadimos -1 al vector de filas y columnas si no es solucion y la casilla anterior si
                    if(cols[j].get(cols[j].size() - 1) != -1)
                        cols[j].add(-1);
                    if(rows[i].get(rows[i].size() - 1) != -1)
                        rows[i].add(-1);
                }
            }
        }
    }

    public void markCell(int x, int y) {
        cellsLeft += board[x][y].changeCell();
    }
}

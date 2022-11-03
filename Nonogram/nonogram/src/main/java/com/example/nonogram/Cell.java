package com.example.nonogram;

import com.example.engine_common.interfaces.IRender;

public class Cell {
    public enum State {
        EMPTY, MARKED, CROSSED, CHECKED    }

    public void init(boolean ans) {
        isAnswer = ans;
        s = State.CROSSED;
    }

    public void render(IRender renderMng, int x, int y, int size){
        int color = 0x1FFFFFF;
        switch (s){
            case EMPTY:
                color = 0x1B9B9B9;
                break;
            case MARKED:
                color = 0x12879C5;
                break;
            case CROSSED:
                color = 0x1FFFFFF;
                break;
            case CHECKED:
                color = 0x1EA2525;
                break;
        }
        if(isAnswer)
            color = 0xFFFFFFFF;
        else
            color = 0xEA2525FF;
        renderMng.setColor(color);
        renderMng.drawRectangle(x*size + x*2 + 10, y*size + y*2 + 35, size, size, true);
        renderMng.setColor(0x1FFFFFF);
    }

    public int changeCell(){
        if(s == State.EMPTY) s=State.MARKED;
        else if(s == State.MARKED) s=State.CROSSED;
        else if(s == State.CROSSED) s=State.EMPTY;

        //Valor para cambiar el contador de casillas restantes
        int retValue = 0;

        if(s==State.CROSSED && isAnswer)
            retValue = -1;
        else if(s == State.MARKED && isAnswer)
            retValue = 1;

        return retValue;
    }

    boolean isAnswer;
    State s;
}
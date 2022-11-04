package com.example.nonogram;

import com.example.engine_common.interfaces.IRender;

public class Cell {
    public enum State {
        EMPTY, MARKED, CROSSED, CHECKED    }

    public void init(boolean ans) {
        isAnswer = ans;
        s = State.EMPTY;
    }

    public void render(IRender renderMng, int x, int y, int size){
        int color = 0x1FFFFFF;
        switch (s){
            case EMPTY:
                color = 0xFFCCCCCC;
                break;
            case MARKED:
                color = 0xFF2140D1;
                break;
            case CROSSED:
                color = 0xFFFFFFFF;
                break;
            case CHECKED:
                color = 0xFFFF2F2B;
                break;
        }
        if(isAnswer)        //hay que quitar esto que es para debug
            color = 0xFFFFFFFF;
        renderMng.setColor(color);
        renderMng.drawRectangle(x, y, size, size, true);
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
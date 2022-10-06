package com.example.nonogram;

public class Cell {
    public enum State {
        EMPTY, MARKED, CROSSED, CHECKED    }

    public void init(){

    }

    public void render(){

    }

    public int changeCell(){
        if(s == State.EMPTY) s=State.MARKED;
        else if(s == State.MARKED) s=State.CROSSED;
        else if(s == State.CROSSED) s=State.EMPTY;

        //Valor para cambiar el contador de casillas restantes
        int retValue = 0;

        if(s==State.CROSSED && isAnswer)
            retValue = -1;

        if(s == State.MARKED && isAnswer)
            retValue = 1;

        return retValue;
    }

    boolean isAnswer;
    State s;
}
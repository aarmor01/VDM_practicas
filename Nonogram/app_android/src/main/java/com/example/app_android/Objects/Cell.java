package com.example.app_android.Objects;

import com.example.app_android.GameManager;
import com.example.engine_android.Modules.RenderAndroid;

public class Cell {
    public enum State {
        EMPTY, MARKED, CROSSED, CHECKED    }

    public void init(boolean ans) {
        isAnswer = ans;
        s = notCheckedS = State.EMPTY;
    }

    public void render(RenderAndroid renderMng, int x, int y, int size){
        int color = 0xFF000000;
        switch (s){
            case EMPTY:
                color = GameManager.getInstance().getColor(GameManager.ColorTypes.auxColor.ordinal());
                break;
            case MARKED:
                color = GameManager.getInstance().getColor(GameManager.ColorTypes.mainColor.ordinal());
                break;
            case CROSSED:
                color = 0xFFFFFFFF;
                break;
            case CHECKED:
                color = GameManager.getInstance().getColor(GameManager.ColorTypes.secondaryColor.ordinal());
                break;
        }
        renderMng.setColor(color);
        renderMng.drawRectangle(x, y, size, size, true);
        if(s == State.CROSSED){
            renderMng.setColor(0xFF000000);
            renderMng.drawRectangle(x, y, size, size, false);
            renderMng.drawLine(x, y, x + size, y + size);
        }
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

    public void markCell(){
        if(s == State.EMPTY) s=State.MARKED;
        else if(s == State.MARKED) s=State.EMPTY;
    }

    public void crossCell(){
        if(s == State.EMPTY) s=State.CROSSED;
        else if(s == State.CROSSED) s=State.EMPTY;
    }

    private boolean isAnswer;
    private State s;
    private State notCheckedS;
    public boolean isAnswer(){ return isAnswer;}
    public State getState(){ return s;}
    public void setChecked(){ notCheckedS = s; s = State.CHECKED;}
    public void unChecked(){ s = notCheckedS; }
}
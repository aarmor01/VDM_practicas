package com.example.app_android.Objects;

import com.example.app_android.GameManager;
import com.example.engine_android.Modules.RenderAndroid;

public class Cell {
    public enum State {EMPTY, MARKED, CROSSED, CHECKED}

    private boolean isAnswer;
    private State state;
    private State previosToCheckState;

    public void init(boolean ans, State st) {
        isAnswer = ans;
        state = previosToCheckState = st;
    }

    public void render(RenderAndroid renderMng, int x, int y, int size) {
        int color = 0xFF000000;
        switch (state) {
            case EMPTY:
                color = GameManager.getInstance().getColor(GameManager.ColorTypes.AUX_COLOR.ordinal());
                break;
            case MARKED:
                color = GameManager.getInstance().getColor(GameManager.ColorTypes.MAIN_COLOR.ordinal());
                break;
            case CROSSED:
                color = 0xFFFFFFFF;
                break;
            case CHECKED:
                color = GameManager.getInstance().getColor(GameManager.ColorTypes.SECONDARY_COLOR.ordinal());
                break;
        }
        renderMng.setColor(color);
        renderMng.drawRectangle(x, y, size, size, true);
        if (state == State.CROSSED) {
            renderMng.setColor(0xFF000000);
            renderMng.drawRectangle(x, y, size, size, false);
            renderMng.drawLine(x, y, x + size, y + size);
        }
    }

    public int changeCell() {
        if (state == State.EMPTY) state = State.MARKED;
        else if (state == State.MARKED) state = State.CROSSED;
        else if (state == State.CROSSED) state = State.EMPTY;

        //Valor para cambiar el contador de casillas restantes
        int retValue = 0;

        if (state == State.CROSSED && isAnswer)
            retValue = -1;
        else if (state == State.MARKED && isAnswer)
            retValue = 1;

        return retValue;
    }

    public void markCell() {
        if (state == State.EMPTY || state == State.CROSSED) state = State.MARKED;
        else if (state == State.MARKED) state = State.EMPTY;
    }

    public void crossCell() {
        if (state == State.EMPTY) state = State.CROSSED;
        else if (state == State.CROSSED) state = State.EMPTY;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public State getState() {
        return state;
    }

    public void setState(State st) {
        state = previosToCheckState = st;
    }

    public void setChecked() {
        previosToCheckState = state;
        state = State.CHECKED;
    }

    public void unChecked() {
        state = previosToCheckState;
    }
}
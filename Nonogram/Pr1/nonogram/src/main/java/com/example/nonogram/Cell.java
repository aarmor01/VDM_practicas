package com.example.nonogram;

import com.example.engine_common.interfaces.IRender;

public class Cell {
    public enum State {EMPTY, MARKED, CROSSED, CHECKED}

    private boolean isAnswer;
    private State state;

    public void init(boolean answer) {
        this.isAnswer = answer;
        this.state = State.EMPTY;
    }

    public void render(IRender renderer, int x, int y, int size) {
        // draw cell color
        renderer.setColor(getCellColor());
        renderer.drawRectangle(x, y, size, size, true);

        // if crossed, we draw
        if (this.state == State.CROSSED) {
            renderer.setColor(0xFF000000);
            renderer.drawRectangle(x, y, size, size, false);
            renderer.drawLine(x, y, x + size, y + size);
        }
    }

    public int changeCell() {
        if (this.state == State.EMPTY) this.state = State.MARKED;
        else if (this.state == State.MARKED) this.state = State.CROSSED;
        else if (this.state == State.CROSSED) this.state = State.EMPTY;

        // valor para cambiar el contador de casillas restantes
        if (this.state == State.CROSSED && this.isAnswer)
            return -1;
        else if (this.state == State.MARKED && this.isAnswer)
            return 1;

        return 0;
    }

    public void setChecked() {
        this.state = State.CHECKED;
    }

    public void uncheck() {
        this.state = State.EMPTY;
    }

    public boolean isAnswer() {
        return this.isAnswer;
    }

    public State getState() {
        return this.state;
    }

    private int getCellColor() {
        int color = 0x1FFFFFF;
        switch (this.state) {
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
        return color;
    }
}
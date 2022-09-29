package com.example.nonogram;

public class Cell {
    public enum State {
        EMPTY, MARKED, UNMARKED
    }

    boolean isAnswer;
    State s;

}
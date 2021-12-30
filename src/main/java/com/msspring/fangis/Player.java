package com.msspring.fangis;

public class Player {
    private Position position;
    private int move;
    private int color;

    public Player(Position position, int move, int color) {
        this.position = position;
        this.move = move;
        this.color = color;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public float getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

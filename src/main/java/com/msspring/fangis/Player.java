package com.msspring.fangis;

public class Player {
    private Position position;

    public Player(Position position) {
        this.position = position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}

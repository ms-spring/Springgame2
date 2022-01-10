package com.msspring.fangis.messages;

import com.msspring.fangis.gameLogic.Position;

public class StatusMessage {
    private Position position;
    private int move;

    public StatusMessage() {
    }

    public StatusMessage(Position position, int move) {
        this.position = position;
        this.move = move;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }
}

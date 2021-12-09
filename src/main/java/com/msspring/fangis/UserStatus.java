package com.msspring.fangis;

public class UserStatus {
    private Position position;
    private Game game;

    public UserStatus(Position position, Game game) {
        this.position = position;
        this.game = game;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public Position getPosition() {
        return position;
    }
}

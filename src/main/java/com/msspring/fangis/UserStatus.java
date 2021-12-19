package com.msspring.fangis;

public class UserStatus {
    private Position position;
    private GameManager gameManager;

    public UserStatus(Position position, GameManager gameManager) {
        this.position = position;
        this.gameManager = gameManager;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setGame(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGame() {
        return gameManager;
    }

    public Position getPosition() {
        return position;
    }
}

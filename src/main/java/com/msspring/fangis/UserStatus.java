package com.msspring.fangis;

public class UserStatus {
    private Position position;

    public UserStatus(Position position) {
        this.position = position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}

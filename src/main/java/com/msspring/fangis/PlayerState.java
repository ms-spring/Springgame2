package com.msspring.fangis;

public class PlayerState {
    private String name;
    private Position position;

    public PlayerState(User user, Player player) {
        this.name = user.getName();
        this.position = player.getPosition();
    }
}

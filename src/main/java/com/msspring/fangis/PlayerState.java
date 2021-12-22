package com.msspring.fangis;

public class PlayerState {
    private String name;
    private Position position;
    private int lobby;

    public PlayerState(User user, Player player) {
        this.name = user.getName();
        this.position = player.getPosition();
        this.lobby = user.getLobby();
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public int getLobby() {
        return lobby;
    }

    @Override
    public String toString() {
        return "PlayerState{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", lobby=" + lobby +
                '}';
    }
}

package com.msspring.fangis;

public class PlayerState {
    private String name;
    private int lobby;
    private Player player;

    public PlayerState(User user, Player player) {
        this.name = user.getName();
        this.lobby = user.getLobby();
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public int getLobby() {
        return lobby;
    }

    public Player getPlayer() {
        return player;
    }
}

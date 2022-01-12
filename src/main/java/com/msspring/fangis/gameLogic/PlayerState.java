package com.msspring.fangis.gameLogic;

public class PlayerState {
    private final String name;
    private final int lobby;
    private final Player player;

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

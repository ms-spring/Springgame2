package com.msspring.fangis.gameLogic;

public class User {
    private final String name;
    private final int lobby;

    public User(String name, int lobby) {
        this.name = name;
        this.lobby = lobby;
    }

    public String getName() {
        return name;
    }

    public int getLobby() {
        return lobby;
    }
}

package com.msspring.fangis;

public class User {
    private String name;
    private int lobby;

    public User(String name, int lobby) {
        this.name = name;
        this.lobby = lobby;
    }

    public String getName() {
        return name;
    }
}

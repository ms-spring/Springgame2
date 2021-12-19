package com.msspring.fangis;

import java.util.HashMap;

public class GameState {
    private HashMap<User, Player> playerMapping;

    public GameState() {
        playerMapping = new HashMap<>();
    }

    public HashMap<User, Player> getPlayerMapping() {
        return playerMapping;
    }
}

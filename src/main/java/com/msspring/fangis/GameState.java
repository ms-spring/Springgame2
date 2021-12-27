package com.msspring.fangis;

import java.util.HashMap;

public class GameState {
    private HashMap<User, Player> playerMapping;
    private Player faenger;

    public GameState() {
        playerMapping = new HashMap<>();
    }

    public HashMap<User, Player> getPlayerMapping() {
        return playerMapping;
    }

    public Player getFaenger() {
        return faenger;
    }
}

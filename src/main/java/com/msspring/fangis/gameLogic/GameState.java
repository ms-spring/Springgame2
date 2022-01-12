package com.msspring.fangis.gameLogic;

import java.util.HashMap;

public class GameState {
    private final HashMap<User, Player> playerMapping;
    private User faenger;


    public GameState() {
        playerMapping = new HashMap<>();

    }

    public HashMap<User, Player> getPlayerMapping() {
        return playerMapping;
    }

    public User getFaenger() {
        return faenger;
    }

    public void setFaenger(User faenger) {
        this.faenger = faenger;
    }


}

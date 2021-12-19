package com.msspring.fangis;

import java.util.HashMap;

public class GameState {
    private HashMap<String, UserStatus> userStates;

    public GameState() {
        userStates = new HashMap<>();
    }

    public HashMap<String, UserStatus> getUserStates() {
        return userStates;
    }
}

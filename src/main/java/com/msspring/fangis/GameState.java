package com.msspring.fangis;

import java.util.HashMap;

public class GameState {
    private HashMap<String,UserStatus> userStates;

    public GameState() {
        userStates = new HashMap<>();
    }

    public GameState(HashMap<String, UserStatus> userStates) {
        this.userStates = userStates;
    }

    public HashMap<String, UserStatus> getUserStates() {
        return userStates;
    }

    public void setUserStates(HashMap<String, UserStatus> userStates) {
        this.userStates = userStates;
    }
}

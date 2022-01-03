package com.msspring.fangis;

import java.util.HashMap;

public class GameState {
    private HashMap<User, Player> playerMapping;
    private User faenger;
    private Long updateTime;

    public GameState() {
        playerMapping = new HashMap<>();
        updateTime = 0L;
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}

package com.msspring.fangis;

import java.util.HashMap;

public class GameState {
    private HashMap<User, Player> playerMapping;
    private User faenger;
    private long updateTime;
    private boolean nonfungable;

    public GameState() {
        playerMapping = new HashMap<>();
        updateTime = 0L;
        nonfungable = false;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isNonfungable() {
        return nonfungable;
    }

    public void setNonfungable(boolean nonfungable) {
        this.nonfungable = nonfungable;
    }
}

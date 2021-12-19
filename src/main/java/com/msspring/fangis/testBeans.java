package com.msspring.fangis;

import org.springframework.beans.factory.annotation.Autowired;

public class testBeans {

    private GameManager gameManager;

    public testBeans() {
    }

    @Autowired
    public testBeans(GameManager gameManagerState) {
        this.gameManager = gameManagerState;
    }

    public GameManager getGameState() {
        return gameManager;
    }

    public void setGameState(GameManager gameManagerState) {
        this.gameManager = gameManagerState;
    }
}



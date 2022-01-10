package com.msspring.fangis.gameLogic;

import org.springframework.stereotype.Component;

//game class implementing 3 lobbies.
@Component
public class GameManager {
    private GameState[] gameStates;

    public GameManager() {
        gameStates = new GameState[3];
        for (int i = 0; i < 3; i++) gameStates[i] = new GameState();
    }

    public GameState[] getGameStates() {
        return gameStates;
    }
}

package com.msspring.fangis;

//game class implementing 3 lobbies.
public class GameManager {

    GameState[] gameStates;

    public GameManager() {
        gameStates = new GameState[3];
        for (int i = 0; i<3 ; i++) gameStates[i] = new GameState();
    }

}

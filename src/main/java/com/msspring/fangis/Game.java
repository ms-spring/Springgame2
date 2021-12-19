package com.msspring.fangis;

import java.util.HashMap;

//game class implementing 3 lobbies.
public class Game {

    GameState[] gameStates;

    public Game() {
        gameStates = new GameState[3];
        for (int i = 0; i<3 ; i++) gameStates[i] = new GameState();
    }

}

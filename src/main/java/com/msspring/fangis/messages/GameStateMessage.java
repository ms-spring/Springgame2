package com.msspring.fangis.messages;

import com.msspring.fangis.gameLogic.PlayerState;

public class GameStateMessage {
    private final PlayerState[] players;


    public GameStateMessage(PlayerState[] players) {
        this.players = players;
    }

    public PlayerState[] getPlayers() {
        return players;
    }

}

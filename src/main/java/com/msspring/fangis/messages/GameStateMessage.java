package com.msspring.fangis.messages;

import com.msspring.fangis.gameLogic.PlayerState;

public class GameStateMessage {
    private final PlayerState[] players;
    private final PlayerState faenger;
    private final boolean nonfungable;

    public GameStateMessage(PlayerState[] players, PlayerState faenger, boolean nonfungable) {
        this.players = players;
        this.faenger = faenger;
        this.nonfungable = nonfungable;
    }

    public PlayerState[] getPlayers() {
        return players;
    }

    public PlayerState getFaenger() {
        return faenger;
    }

    public boolean isNonfungable() {
        return nonfungable;
    }
}

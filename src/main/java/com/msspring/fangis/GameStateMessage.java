package com.msspring.fangis;

public class GameStateMessage {
    private PlayerState[] players;
    private PlayerState faenger;
    private boolean nonfungable;

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

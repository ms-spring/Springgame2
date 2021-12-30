package com.msspring.fangis;

public class GameStateMessage {
    private PlayerState[] players;
    private PlayerState faenger;

    public GameStateMessage(PlayerState[] players, PlayerState faenger) {
        this.players = players;
        this.faenger = faenger;
    }

    public PlayerState[] getPlayers() {
        return players;
    }

    public PlayerState getFaenger() {
        return faenger;
    }
}

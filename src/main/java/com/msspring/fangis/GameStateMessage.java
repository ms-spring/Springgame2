package com.msspring.fangis;

public class GameStateMessage {
    private PlayerState[] players;
    private Player faenger;

    public GameStateMessage(PlayerState[] players, Player faenger) {
        this.players = players;
    }

    public PlayerState[] getPlayers() {
        return players;
    }

    public Player getFaenger() {
        return faenger;
    }
}

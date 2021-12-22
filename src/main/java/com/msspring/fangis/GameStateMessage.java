package com.msspring.fangis;

import java.util.List;
import java.util.Map;

public class GameStateMessage {
    private Map<String,PlayerState> players;

    public GameStateMessage(Map<String,PlayerState> players) {
        this.players = players;
    }
}

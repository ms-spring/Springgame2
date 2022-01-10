package com.msspring.fangis.messages;

import javax.validation.constraints.NotEmpty;

public class UserNameMessage {
    @NotEmpty
    private String name;
    private int lobby;

    public UserNameMessage(String name, int lobby) {
        this.name = name;
        this.lobby = lobby;
    }

    public int getLobby() {
        return lobby;
    }

    public void setLobby(int lobby) {
        this.lobby = lobby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

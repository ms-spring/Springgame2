package com.msspring.fangis;

public class StatusMessage {

    private Player status;

    public StatusMessage() {
    }

    public StatusMessage(Player userstatus) {
        this.status = userstatus;
    }

    public Player getStatus() {
        return status;
    }

    public void setStatus(Player status) {
        this.status = status;
    }
}

package com.msspring.fangis;

public class StatusMessage {

    private UserStatus status;

    public StatusMessage() {
    }

    public StatusMessage(UserStatus userstatus) {
        this.status = userstatus;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}

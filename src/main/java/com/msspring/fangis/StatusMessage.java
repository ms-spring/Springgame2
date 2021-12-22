package com.msspring.fangis;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class StatusMessage {
    @Valid
    private Position position;

    public StatusMessage() {
    }

    public StatusMessage(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}

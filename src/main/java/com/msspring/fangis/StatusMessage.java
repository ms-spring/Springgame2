package com.msspring.fangis;

import javax.validation.constraints.NotNull;

public class StatusMessage {
    @NotNull
    private Position position;

    public StatusMessage(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}

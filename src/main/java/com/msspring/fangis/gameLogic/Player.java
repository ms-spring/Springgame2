package com.msspring.fangis.gameLogic;

public class Player {
    private Position position;
    private int move;
    private int color;
    private int bananas;
    private boolean isfaenger;
    private long whenFunged;


    public Player(Position position, int move, int color, int bananas) {
        this.position = position;
        this.move = move;
        this.color = color;
        this.bananas = bananas;
        isfaenger=false;
        whenFunged = 0L;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBananas() {
        return bananas;
    }

    public void setBananas(int bananas) {
        this.bananas = bananas;
    }

    public boolean isIsfaenger() {
        return isfaenger;
    }

    public void setIsfaenger(boolean isfaenger) {
        this.isfaenger = isfaenger;
    }

    public long getWhenFunged() {
        return whenFunged;
    }

    public void setWhenFunged(long whenFunged) {
        this.whenFunged = whenFunged;
    }

    public double computeDist(Player p) {
        double dx = this.position.getX() - p.position.getX();
        double dy = this.position.getY() - p.position.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}

package com.msspring.fangis.gameLogic;

public class Player {
    private Position position;
    private int move;
    private int color;
    private int bananas;

    public Player(Position position, int move, int color, int bananas) {
        this.position = position;
        this.move = move;
        this.color = color;
        this.bananas = bananas;
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

    public double computeDist(Player p) {
        return Math.sqrt(Math.pow(this.position.getX()-p.position.getX(),2)+ Math.pow(this.position.getY()-p.position.getY(),2));
    };
}

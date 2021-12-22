package com.msspring.fangis;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Size;

public class Position {
    @Size(min = 0, max = 800)
    private double x;
    @Size(min = 0, max = 600)
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

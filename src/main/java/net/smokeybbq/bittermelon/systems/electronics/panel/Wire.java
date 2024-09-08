package net.smokeybbq.bittermelon.systems.electronics.panel;

import java.awt.geom.Point2D;

public class Wire {
    private Point2D firstPoint;
    private Point2D secondPoint;
    private int color;

    public Wire(int color) {
        this.color = color;
    }

    public Point2D getFirstPoint() {
        return firstPoint;
    }

    public Point2D getSecondPoint() {
        return secondPoint;
    }

    public int getColor() {
        return color;
    }

    public void setFirstPoint(Point2D firstPoint) {
        this.firstPoint = firstPoint;
    }

    public void setSecondPoint(Point2D secondPoint) {
        this.secondPoint = secondPoint;
    }

    public void setColor(int color) {
        this.color = color;
    }
}


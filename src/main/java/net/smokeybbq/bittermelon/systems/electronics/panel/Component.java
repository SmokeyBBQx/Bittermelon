package net.smokeybbq.bittermelon.systems.electronics.panel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Component {
    private int height;
    private int width;
    private Point2D location;
    private List<Port> ports = new ArrayList<>();

    public Component() {

    }

    public List<Port> getPorts() {
        return ports;
    }

    public Point2D getLocation() {
        return location;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

}

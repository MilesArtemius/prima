package classes.graph;

import java.awt.geom.Point2D;

public class Node {
    private Point2D position;
    private String name;

    public Node(Point2D position, String name) {
        this.position = position;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
}

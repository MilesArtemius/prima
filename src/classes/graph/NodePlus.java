package classes.graph;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class NodePlus extends Node implements Serializable {

    private static final long serialVersionUID = 4L;
    private Point2D position;

    public NodePlus(Point2D position, String name) {
        super(name);
        this.position = position;
    }
    public NodePlus(String name) {
        super(name);
        this.position = new Point2D.Double(-1, -1);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
}

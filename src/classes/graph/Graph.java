package classes.graph;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    public List<Node> getNodes() {
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(new Node(new Point2D.Double(1, 2), "1"));
        nodes.add(new Node(new Point2D.Double(10, 20), "2"));
        nodes.add(new Node(new Point2D.Double(100, 200), "3"));
        nodes.add(new Node(new Point2D.Double(278, 347), "4"));
        return nodes;
    }

    public List<Ark> getArks() {
        LinkedList<Ark> arks = new LinkedList<>();
        arks.add(new Ark(getNodes().get(0), getNodes().get(1), 10.0));
        arks.add(new Ark(getNodes().get(2), getNodes().get(3), 27.5));
        return arks;
    }

    private void setPosition(int total, int number) {
        double ang = 360.0 / total * number;
        double trueAng = Math.toRadians(90) - Math.toRadians(ang);
        int x = 401 + (int) (Math.cos(trueAng) * 200);
        int y = 301 - (int) (Math.sin(trueAng) * 200);
    }
}

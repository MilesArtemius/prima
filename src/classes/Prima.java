package classes;

import classes.graph.Graph;
import classes.shapes.GraphShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Prima {
    static Graph prepareInput() {
        Graph graph = new Graph();

        graph.addNode(new Point2D.Double(10, 20), "1");
        graph.addNode(new Point2D.Double(40, 60), "2");
        graph.addNode(new Point2D.Double(100, 200), "3");
        graph.addNode(new Point2D.Double(278, 347), "4");

        graph.addArk("1", "2", 8);
        graph.addArk("3", "4", 8);

        return graph;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("GraphShape");
        f.setMinimumSize(new Dimension(801, 601));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GraphShape shape = new GraphShape(prepareInput());

        f.add(shape);
        f.setVisible(true);
        f.pack();
    }
}

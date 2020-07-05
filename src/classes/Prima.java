package classes;

import classes.graph.Graph;
import classes.shapes.GraphShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Prima {
    static Graph prepareInput() {
        Graph graph = new Graph();

        graph.addNode(new Point2D.Double(200, 400), "A");
        graph.addNode(new Point2D.Double(800, 400), "B");
        graph.addNode(new Point2D.Double(500, 700), "C");
        graph.addNode(new Point2D.Double(500, 100), "D");

        graph.addArk("A", "B", 8);
        graph.addArk("B", "C", 8);
        graph.addArk("C", "D", 8);

        return graph;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("GraphShape");
        f.setMinimumSize(new Dimension(1200, 900));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GraphShape shape = new GraphShape(prepareInput());

        f.add(shape);
        f.setVisible(true);
        f.pack();
    }
}

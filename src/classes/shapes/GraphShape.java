package classes.shapes;

import classes.graph.Ark;
import classes.graph.Graph;
import classes.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class GraphShape extends JPanel {
    Graph graph;
    Graph step;
    LinkedList<NodeShape> nodes;
    LinkedList<ArkShape> arks;

    public GraphShape(Graph graph) {
        this.graph = graph;
        nodes = new LinkedList<>();
        arks = new LinkedList<>();

        for (Node node: graph.getNodes()) {
            NodeShape ns = new NodeShape(node, getSizeModifier());
            nodes.push(ns);
        }

        for (Ark ark: graph.getArks()) {
            ArkShape ns = new ArkShape(ark, getSizeModifier());
            arks.push(ns);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (NodeShape node: nodes) if (node.contains(e.getPoint()) && node.onClick(e)) break;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        for (ArkShape s : arks) {
            g2d.draw(s);
        }
        for (NodeShape s : nodes) {
            g2d.draw(s);
        }
    }

    private int getSizeModifier() {
        return 2;
    }
}

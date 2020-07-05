package classes.shapes;

import classes.graph.Ark;
import classes.graph.Graph;
import classes.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Predicate;

public class GraphShape extends JPanel {
    Graph graph;
    Graph step;
    private LinkedList<NodeShape> nodes;
    private LinkedList<ArkShape> arks;

    public GraphShape(Graph graph) {
        this.graph = graph;
        nodes = new LinkedList<>();
        arks = new LinkedList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse pressed");
                for (NodeShape node: nodes) if (node.contains(e.getPoint()) && node.pressMouse(GraphShape.this, e)) return;
                for (ArkShape ark: arks) if (ark.contains(e.getPoint()) && ark.pressMouse(GraphShape.this, e)) return;
                if (e.isPopupTrigger()) {
                    MenuPopUp popUp = new MenuPopUp(new Point2D.Double(e.getX(), e.getY()));
                    popUp.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("Mouse released");
                for (NodeShape node: nodes) if (node.contains(e.getPoint()) && node.releaseMouse(GraphShape.this, e)) return;
                for (ArkShape ark: arks) if (ark.contains(e.getPoint()) && ark.releaseMouse(GraphShape.this, e)) return;
                if (e.isPopupTrigger()) {
                    MenuPopUp popUp = new MenuPopUp(new Point2D.Double(e.getX(), e.getY()));
                    popUp.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }



    private void refillGraph() {
        nodes.clear();
        arks.clear();

        for (Node node: graph.getNodes()) nodes.push(new NodeShape(node));
        for (Ark ark: graph.getArks()) arks.push(new ArkShape(ark));
    }



    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        refillGraph();

        Graphics2D g2d = (Graphics2D) graphics;
        for (ArkShape s : arks) {
            s.invalidate(this);
            g2d.draw(s);
        }
        for (NodeShape s : nodes) {
            s.invalidate(this);
            g2d.draw(s);
        }
    }

    public int getSizeModifier() {
        return Math.min(getSize().width, getSize().height) / 100;
    }

    public Graph getGraph() {
        return graph;
    }



    private class MenuPopUp extends JPopupMenu {
        public MenuPopUp(Point2D position) {
            JMenuItem item = new JMenuItem("Create Node");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GraphShape.this.getGraph().addNode(position, "Node " + (new Random()).nextInt());
                    GraphShape.this.repaint();
                }
            });
            add(item);
        }
    }
}

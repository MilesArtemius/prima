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
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Random;

public class GraphShape extends JPanel {
    private Graph graph;
    private Graph step;
    private LinkedList<NodeShape> nodes;
    private LinkedList<ArkShape> arks;
    private Node movingNode;

    public GraphShape(Graph graph) {
        this.graph = graph;
        nodes = new LinkedList<>();
        arks = new LinkedList<>();

        MouseAdapter adapter = new MouseAdapter() {
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

            @Override
            public void mouseDragged(MouseEvent e) {
                if (movingNode != null) for (NodeShape shape: nodes) if (shape.node == movingNode) shape.movedMouse(GraphShape.this, e);
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    public void registerMoving(Node node) {
        if (movingNode == null) {
            System.out.println("New node moving: " + node.getName());
            movingNode = node;
        }
    }

    public void unRegisterMoving(Node node) {
        if (movingNode == node) {
            System.out.println("Node stopped: " + node.getName());
            movingNode = null;
        }
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
        g2d.setPaint(Color.DARK_GRAY);
        for (ArkShape s : arks) s.invalidate(this, g2d);
        for (NodeShape s : nodes) s.invalidate(this, g2d);
    }

    public int getSizeModifier() {
        return Math.min(getSize().width, getSize().height) / 100;
    }

    public Graph getGraph() {
        return graph;
    }



    public static void drawCenteredString(Graphics2D g, String string, double x, double y, double width, double height) {
        Rectangle2D fontMetrics = g.getFontMetrics(g.getFont()).getStringBounds(string, g);
        double mod = (fontMetrics.getWidth() > fontMetrics.getHeight()) ? (width / fontMetrics.getWidth()) : (height / fontMetrics.getHeight());
        double fontSize = mod * g.getFont().getSize();
        Font font = g.getFont().deriveFont((float) fontSize);

        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        x += (width - metrics.stringWidth(string)) / 2;
        y += ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(string, (float) x, (float) y);
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

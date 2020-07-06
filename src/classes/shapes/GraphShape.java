package classes.shapes;

import classes.Log;
import classes.Settings;
import classes.dial.NodeNameDialog;
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
    private LinkedList<NodeShape> nodes;
    private LinkedList<ArkShape> arks;
    private Node movingNode;

    public GraphShape() {
        graph = new Graph();
        nodes = new LinkedList<>();
        arks = new LinkedList<>();

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Log.in().say("Mouse pressed at (", e.getX(), ", ", e.getY(), ")");
                for (NodeShape node: nodes) if (node.contains(e.getPoint()) && node.pressMouse(GraphShape.this, e)) return;
                for (ArkShape ark: arks) if (ark.contains(e.getPoint()) && ark.pressMouse(GraphShape.this, e)) return;
                if (e.isPopupTrigger()) {
                    MenuPopUp popUp = new MenuPopUp(new Point2D.Double(e.getX(), e.getY()));
                    popUp.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Log.in().say("Mouse released at (", e.getX(), ", ", e.getY(), ")");
                for (NodeShape node: nodes) if (node.contains(e.getPoint()) && node.releaseMouse(GraphShape.this, e)) return;
                for (ArkShape ark: arks) if (ark.contains(e.getPoint()) && ark.releaseMouse(GraphShape.this, e)) return;
                if (e.isPopupTrigger()) {
                    MenuPopUp popUp = new MenuPopUp(new Point2D.Double(e.getX(), e.getY()));
                    popUp.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (movingNode != null) for (NodeShape shape: nodes) if (shape.getNode() == movingNode) shape.movedMouse(GraphShape.this, e);
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }



    public void setGraph(Graph graph) {
        this.graph = graph;
    }



    public void registerMoving(Node node, MouseEvent e) {
        if (movingNode == null) {
            Log.in().say("Node '", node, "' moving from (", e.getX(), ", ", e.getY(), ")");
            movingNode = node;
        }
    }

    public void unRegisterMoving(Node node, MouseEvent e) {
        if (movingNode == node) {
            Log.in().say("Node '", node, "' stopped at (", e.getX(), ", ", e.getY(), ")");
            movingNode = null;
        }
    }


    /**
     * Use GraphShape.repaint() method to redraw graph!
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        nodes.clear();
        arks.clear();

        Graphics2D g2d = (Graphics2D) graphics;
        for (Ark ark: graph.getArks()) arks.push(new ArkShape(ark, this, g2d));
        for (Node node: graph.getNodes()) nodes.push(new NodeShape(node, this, g2d));
    }

    public int getSizeModifier() {
        return Math.min(getSize().width, getSize().height) / Settings.getInt("graph_shape_size_modifier");
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
            JMenuItem item = new JMenuItem(Settings.getString("create_node_action"));
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    NodeNameDialog dialog = new NodeNameDialog(SwingUtilities.getWindowAncestor(GraphShape.this), Settings.getString("create_node_dialog_name"));
                    dialog.setListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String nodeName = Settings.getString("create_node_dialog_default_node_name",
                                    (new Random()).nextInt() % Settings.getLong("graph_shape_random_node_name_length"));
                            if (!dialog.getResult().equals("")) nodeName = dialog.getResult();
                            dialog.dispose();

                            Log.in().say("Created new node: '", nodeName, "'");
                            GraphShape.this.getGraph().addNode(position, nodeName);
                            GraphShape.this.repaint();
                        }
                    });
                    dialog.pack();
                    dialog.setLocationRelativeTo(GraphShape.this);
                    dialog.setVisible(true);
                }
            });
            add(item);
        }
    }
}

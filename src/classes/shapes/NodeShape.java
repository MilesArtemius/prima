package classes.shapes;

import classes.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class NodeShape extends Ellipse2D.Double implements GraphPart {
    private static final int gap = 7;
    private static final int stroke = 2;

    Node node;

    public NodeShape(Node node) {
        this.node = node;
    }

    @Override
    public void invalidate(GraphShape parent, Graphics2D graphics) {
        Point2D position = node.getPosition();
        double diameter = gap * parent.getSizeModifier();

        setFrame(position.getX() - diameter/2, position.getY() - diameter/2, diameter, diameter);

        Paint paint = graphics.getPaint();
        graphics.setPaint(Color.DARK_GRAY);
        graphics.fill(this);

        setFrame(getX() + stroke, getY() + stroke, getWidth() - 2*stroke, getHeight() - 2*stroke);

        graphics.setPaint(Color.yellow);
        graphics.fill(this);

        graphics.setPaint(Color.DARK_GRAY);
        double textRadius = Math.sqrt(Math.pow(diameter / 2, 2) / 2);
        GraphShape.drawCenteredString(graphics, node.getName(), position.getX() - textRadius, position.getY() - textRadius, textRadius * 2, textRadius * 2);

        graphics.setPaint(paint);
    }

    @Override
    public boolean pressMouse(GraphShape parent, MouseEvent e) {
        System.out.println("Mouse pressed on " + node.getName());
        if (e.isPopupTrigger()) {
            MenuPopUp popUp = new MenuPopUp(parent);
            popUp.show(e.getComponent(), e.getX(), e.getY());
        } else parent.registerMoving(this.node);
        return true;
    }

    @Override
    public boolean releaseMouse(GraphShape parent, MouseEvent e) {
        System.out.println("Mouse released from " + node.getName());
        if (e.isPopupTrigger()) {
            MenuPopUp popUp = new MenuPopUp(parent);
            popUp.show(e.getComponent(), e.getX(), e.getY());
        }
        parent.unRegisterMoving(this.node);
        return true;
    }

    public void movedMouse(GraphShape parent, MouseEvent e) {
        System.out.println("Node " + node.getName() + " moved to (" + e.getX() + ", " + e.getY() + ")");
        node.setPosition(e.getPoint());
        parent.repaint();
    }



    private class MenuPopUp extends JPopupMenu {
        public MenuPopUp(GraphShape parent) {
            JMenuItem remove = new JMenuItem("Remove Node");
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.getGraph().deleteNode(NodeShape.this.node.getName());
                    parent.repaint();
                }
            });
            add(remove);

            JMenu connect = new JMenu("Connect with...");
            for (Node node: parent.getGraph().getNodes()) {
                JMenuItem item = new JMenuItem(node.getName());
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("connecting " + NodeShape.this.node.getName() + " with " + node.getName());
                        parent.getGraph().addArk(NodeShape.this.node.getName(), node.getName(), 10);
                        parent.repaint();
                    }
                });
                connect.add(item);
            }
            add(connect);
        }
    }
}

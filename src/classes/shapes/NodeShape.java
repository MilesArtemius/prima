package classes.shapes;

import classes.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class NodeShape extends Ellipse2D.Double implements GraphPart {
    private static final int gap = 5;

    Node node;

    public NodeShape(Node node) {
        this.node = node;
    }

    @Override
    public void invalidate(GraphShape parent) {
        Point2D position = node.getPosition();
        double diameter = gap * parent.getSizeModifier();
        setFrame(position.getX() - diameter/2, position.getY() - diameter/2, diameter, diameter);
    }

    @Override
    public boolean pressMouse(GraphShape parent, MouseEvent e) {
        System.out.println("Mouse pressed on " + node.getName());
        if (e.isPopupTrigger()) {
            MenuPopUp popUp = new MenuPopUp(parent);
            popUp.show(e.getComponent(), e.getX(), e.getY());
        }
        return true;
    }

    @Override
    public boolean releaseMouse(GraphShape parent, MouseEvent e) {
        System.out.println("Mouse released from " + node.getName());
        if (e.isPopupTrigger()) {
            MenuPopUp popUp = new MenuPopUp(parent);
            popUp.show(e.getComponent(), e.getX(), e.getY());
        }
        return true;
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

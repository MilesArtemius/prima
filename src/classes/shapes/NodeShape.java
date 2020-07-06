package classes.shapes;

import classes.Settings;
import classes.dial.ArkWeightDialog;
import classes.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.text.ParseException;

public class NodeShape extends Ellipse2D.Double implements GraphPart {
    Node node;

    public NodeShape(Node node) {
        this.node = node;
    }

    @Override
    public void invalidate(GraphShape parent, Graphics2D graphics, boolean highlight) {
        Point2D position = node.getPosition();
        double diameter = Settings.getInt("node_shape_gap") * parent.getSizeModifier();
        int stroke = Settings.getInt("node_shape_stroke");

        setFrame(position.getX() - diameter/2, position.getY() - diameter/2, diameter, diameter);

        Paint paint = graphics.getPaint();
        graphics.setPaint(highlight ? Color.BLACK : Color.DARK_GRAY);
        graphics.fill(this);

        setFrame(getX() + stroke, getY() + stroke, getWidth() - 2*stroke, getHeight() - 2*stroke);

        graphics.setPaint(highlight ? Color.GREEN : Color.YELLOW);
        graphics.fill(this);

        graphics.setPaint(highlight ? Color.BLACK : Color.DARK_GRAY);
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
            JMenuItem remove = new JMenuItem(Settings.getString("remove_node_action"));
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.getGraph().deleteNode(NodeShape.this.node.getName());
                    parent.repaint();
                }
            });
            add(remove);

            JMenu connect = new JMenu(Settings.getString("create_ark_action"));
            for (Node node: parent.getGraph().getNodes()) {
                if (node == NodeShape.this.node) continue;
                JMenuItem item = new JMenuItem(node.getName());
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArkWeightDialog dialog = new ArkWeightDialog(SwingUtilities.getWindowAncestor(parent), Settings.getString("create_ark_dialog_name"));
                        dialog.setListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    int arkWeight = dialog.getResult();
                                    dialog.dispose();

                                    System.out.println("Connecting " + NodeShape.this.node.getName() + " with " + node.getName());
                                    parent.getGraph().addArk(NodeShape.this.node.getName(), node.getName(), arkWeight);
                                    parent.repaint();
                                } catch (ParseException pe) {
                                    pe.printStackTrace();
                                }
                            }
                        });
                        dialog.pack();
                        dialog.setLocationRelativeTo(parent);
                        dialog.setVisible(true);
                    }
                });
                connect.add(item);
            }
            add(connect);
        }
    }
}

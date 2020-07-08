package classes.shapes;

import classes.Log;
import classes.Settings;
import classes.dial.ArkWeightDialog;
import classes.dial.NodeNameDialog;
import classes.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.util.Random;

public class NodeShape extends Ellipse2D.Double {
    private Node node;
    private double radius;


    public NodeShape(Node node, GraphShape parent, Graphics2D graphics) {
        this.node = node;

        Point2D position = node.getPosition();
        radius = Settings.getLong("node_shape_gap") * parent.getSizeModifier() / 2.0;
        int stroke = Settings.getInt("node_shape_stroke");

        setFrame(position.getX() - radius, position.getY() - radius, radius*2, radius*2);

        graphics.setPaint(node.isHidden() ? Settings.getColor("node_shape_stroke_hidden_color") : Settings.getColor("node_shape_stroke_color"));
        graphics.fill(this);

        setFrame(getX() + stroke, getY() + stroke, getWidth() - 2*stroke, getHeight() - 2*stroke);

        graphics.setPaint(node.isHidden() ? Settings.getColor("node_shape_hidden_color") : Settings.getColor("node_shape_color"));
        graphics.fill(this);

        graphics.setPaint(node.isHidden() ? Settings.getColor("node_shape_text_hidden_color") : Settings.getColor("node_shape_text_color"));
        double textRadius = Math.sqrt(Math.pow(radius, 2) / 2);
        GraphShape.drawCenteredString(graphics, node.getName(), position.getX() - textRadius, position.getY() - textRadius, textRadius * 2, textRadius * 2);
    }

    public Node getNode() {
        return node;
    }



    public boolean pressMouse(GraphShape parent, MouseEvent e, Point2D absolute) {
        Log.cui().say("Mouse pressed on node '", node, "'");
        if (SwingUtilities.isRightMouseButton(e)) {
            MenuPopUp popUp = new MenuPopUp(parent);
            popUp.show(e.getComponent(), (int) absolute.getX(), (int) absolute.getY());
        } else parent.registerMoving(this.node, e);
        return true;
    }

    public boolean releaseMouse(GraphShape parent, MouseEvent e, Point2D absolute) {
        Log.cui().say("Mouse released from node '", node, "'");
        if (SwingUtilities.isLeftMouseButton(e)) parent.unRegisterMoving(this.node, e);
        return true;
    }

    public void movedMouse(GraphShape parent, MouseEvent e, Point2D absolute) {
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

            JMenuItem rename = new JMenuItem(Settings.getString("rename_node_action"));
            rename.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    NodeNameDialog dialog = new NodeNameDialog(SwingUtilities.getWindowAncestor(parent),
                            Settings.getString("rename_node_dialog_name"), true);
                    dialog.setListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!dialog.getResult().equals("")) {
                                String nodeName = dialog.getResult();
                                dialog.dispose();

                                Log.cui().say("Renamed node: '", NodeShape.this.node.getName(), "' to '" + nodeName + "'");
                                parent.getGraph().changeNode(NodeShape.this.node.getName(), nodeName);
                                parent.repaint();
                            }
                        }
                    });
                    dialog.pack();
                    dialog.setLocationRelativeTo(parent);
                    dialog.setVisible(true);
                }
            });
            add(rename);

            JMenu connect = new JMenu(Settings.getString("create_ark_action"));
            for (Node node: parent.getGraph().getNodes()) {
                if ((node == NodeShape.this.node) || (NodeShape.this.node.getArkTo(node) != null)) continue;
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

                                    Log.cui().say("Connecting node '", NodeShape.this.node, "' with node '", node, "'");
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

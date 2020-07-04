package classes.shapes;

import classes.graph.Node;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class NodeShape extends Ellipse2D.Double {
    private static final int gap = 10;

    MouseAdapter adapter;
    Node node;

    public NodeShape(Node node, int sizeModifier) {
        super(node.getPosition().getX() - (gap * sizeModifier) / 2.0, node.getPosition().getY() - (gap * sizeModifier) / 2.0,
                gap * sizeModifier, gap * sizeModifier);
        this.node = node;
        adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("Mouse clicked on " + node.getName());
            }
        };
    }

    public void setOnClickListener(MouseAdapter ma) {
        this.adapter = ma;
    }

    public void resetOnClickListener() {
        this.adapter = null;
    }







    public boolean onClick(MouseEvent e) {
        if (adapter == null) return false;
        adapter.mouseClicked(e);
        return true;
    }
}

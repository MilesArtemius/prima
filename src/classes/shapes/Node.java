package classes.shapes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Node extends Ellipse2D.Double {
    private static final int gap = 10;

    MouseAdapter adapter;
    String name;

    public Node(Point2D center, String name, int sizeModifier) {
        super(center.getX(), center.getY(), gap * sizeModifier, gap * sizeModifier);
        this.name = name;
        // adapter = null;
        adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("Mouse clicked on " + name);
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

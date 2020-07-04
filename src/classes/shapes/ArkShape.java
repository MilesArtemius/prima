package classes.shapes;

import classes.graph.Ark;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ArkShape extends Polygon {
    private static final int gap = 3;

    MouseAdapter adapter;
    Ark ark;

    public ArkShape(Ark ark, int sizeModifier) {
        addPoint((int) ark.getStart().getPosition().getX(), (int) ark.getStart().getPosition().getY());
        addPoint((int) ark.getEnd().getPosition().getX(), (int) ark.getEnd().getPosition().getY());
        adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("Mouse clicked on " + ark.getStart().getName() + " - > " + ark.getEnd().getName());
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

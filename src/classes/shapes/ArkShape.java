package classes.shapes;

import classes.graph.Ark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class ArkShape extends Polygon implements GraphPart {
    private static final int gap = 2;

    Ark ark;

    public ArkShape(Ark ark) {
        this.npoints = 2; // 4
        this.ark = ark;
    }

    @Override
    public void invalidate(GraphShape parent) {
        xpoints[0] = (int) ark.getStart().getPosition().getX();
        xpoints[1] = (int) ark.getEnd().getPosition().getX();
        ypoints[0] = (int) ark.getStart().getPosition().getY();
        ypoints[1] = (int) ark.getEnd().getPosition().getY();
        invalidate();
    }

    @Override
    public boolean pressMouse(GraphShape parent, MouseEvent e) {
        System.out.println("Mouse pressed on " + ark.getStart().getName() + " - > " + ark.getEnd().getName());
        if (e.isPopupTrigger()) {
            MenuPopUp popUp = new MenuPopUp(parent);
            popUp.show(e.getComponent(), e.getX(), e.getY());
        }
        return true;
    }

    @Override
    public boolean releaseMouse(GraphShape parent, MouseEvent e) {
        System.out.println("Mouse released from " + ark.getStart().getName() + " - > " + ark.getEnd().getName());
        if (e.isPopupTrigger()) {
            MenuPopUp popUp = new MenuPopUp(parent);
            popUp.show(e.getComponent(), e.getX(), e.getY());
        }
        return true;
    }



    private class MenuPopUp extends JPopupMenu {
        public MenuPopUp(GraphShape parent) {
            JMenuItem item = new JMenuItem("Remove Ark");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.getGraph().deleteArk(ArkShape.this.ark.getStart(), ArkShape.this.ark.getEnd());
                    parent.repaint();
                }
            });
            add(item);
        }
    }
}

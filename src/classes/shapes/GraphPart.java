package classes.shapes;

import java.awt.event.MouseEvent;

public interface GraphPart {
    void invalidate(GraphShape parent);
    boolean pressMouse(GraphShape parent, MouseEvent e);
    boolean releaseMouse(GraphShape parent, MouseEvent e);
}

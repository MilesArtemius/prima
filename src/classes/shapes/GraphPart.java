package classes.shapes;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface GraphPart {
    void invalidate(GraphShape parent, Graphics2D graphics);
    boolean pressMouse(GraphShape parent, MouseEvent e);
    boolean releaseMouse(GraphShape parent, MouseEvent e);
}

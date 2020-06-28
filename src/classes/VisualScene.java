package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VisualScene extends JPanel {
    private BufferedImage print() {
        BufferedImage bi = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        this.paint(g);
        return bi;
    }
}

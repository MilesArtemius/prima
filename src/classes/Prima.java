package classes;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.util.HashMap;

public class Prima {
    static HashMap<Character, HashMap<Character, Double>> prepareInout() {
        HashMap<Character, HashMap<Character, Double>> nodes = new HashMap<>();

        HashMap<Character, Double> aArks = new HashMap<>();
        aArks.put('B', 12.0);
        aArks.put('C', 1.0);

        HashMap<Character, Double> bArks = new HashMap<>();
        bArks.put('D', 5.0);

        HashMap<Character, Double> cArks = new HashMap<>();
        cArks.put('D', 7.0);

        HashMap<Character, Double> dArks = new HashMap<>();

        nodes.put('A', aArks);
        nodes.put('B', bArks);
        nodes.put('C', cArks);
        nodes.put('D', dArks);

        return nodes;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Graph");
        f.setMinimumSize(new Dimension(801, 601));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        VisualScene panel = new VisualScene();
        LayerUI<VisualScene> layerUI = new GraphView(prepareInout());
        ((GraphView) layerUI).setSolvation("ABD");
        JLayer<VisualScene> jLayer = new JLayer<>(panel, layerUI);

        f.add(jLayer);
        f.setVisible(true);
        f.pack();
    }
}

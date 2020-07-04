package classes;

import classes.graph.Graph;
import classes.shapes.GraphShape;

import javax.swing.*;
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
        JFrame f = new JFrame("GraphShape");
        f.setMinimumSize(new Dimension(801, 601));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*VisualScene panel = new VisualScene(prepareInout());
        panel.setSolvation("ABD");*/

        Graph graph = new Graph();
        GraphShape shape = new GraphShape(graph);

        f.add(shape);
        f.setVisible(true);
        f.pack();
    }
}

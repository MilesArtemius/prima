package classes;

import classes.algorithm.PrimaAlgorithm;
import classes.graph.Graph;
import classes.io.SavedGraph;
import classes.shapes.GraphShape;
import classes.PrimaVisual;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Prima {
    private static PrimaVisual visual;

    public static Graph prepareInput() {
        Graph graph = new Graph();

        /*graph.addNode(new Point2D.Double(200, 400), "A");
        graph.addNode(new Point2D.Double(800, 400), "B");
        graph.addNode(new Point2D.Double(500, 700), "C");
        graph.addNode(new Point2D.Double(500, 100), "D");

        graph.addArk("A", "B", 6);
        graph.addArk("B", "C", 7);
        graph.addArk("C", "D", 8);
        graph.addArk("A", "D", 5);*/


        graph.addNode(new Point2D.Double(200, 400), "A");
        graph.addNode(new Point2D.Double(300, 600), "B");
        graph.addNode(new Point2D.Double(500, 600), "C");
        graph.addNode(new Point2D.Double(400, 400), "D");
        graph.addNode(new Point2D.Double(600, 400), "E");
        graph.addNode(new Point2D.Double(400, 200), "F");

        graph.addArk("A", "B", 8);
        graph.addArk("A", "D", 3);
        graph.addArk("A", "F", 10);
        graph.addArk("B", "C", 9);
        graph.addArk("B", "D", 7);
        graph.addArk("C", "E", 4);
        graph.addArk("C", "D", 3);
        graph.addArk("E", "D", 13);
        graph.addArk("E", "F", 5);
        graph.addArk("F", "D", 1);



        //пример сохранения-загрузки
        //SavedGraph sg = new SavedGraph(graph);
        //sg.save();
        //Graph ng = sg.load();

        return graph;
    }

    public static void main(String[] args) {
        Log.in().say("Uptime started");
        Settings.setup();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame(Settings.getString("app_name"));
        f.setMinimumSize(new Dimension(Settings.getInt("default_screen_width"), Settings.getInt("default_screen_height")));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*Graph graph = prepareInput();
        GraphShape shape = new GraphShape();
        shape.setGraph(graph);

        PrimaAlgorithm alg = new PrimaAlgorithm();
        alg.solve(graph);

        f.add(shape);*/

        visual = new PrimaVisual(f);

        f.setContentPane(visual.getMainPanel());

        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);
    }

    public static PrimaVisual getVisual() {
        return visual;
    }
}

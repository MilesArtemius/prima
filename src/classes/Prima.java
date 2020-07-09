package classes;

import classes.graph.Graph;
import classes.graph.NodePlus;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Prima {
    private static PrimaVisual visual;

    public static Graph prepareInput() { // TODO: replace with graph samples
        Graph graph = new Graph();

        graph.addNode(new NodePlus(new Point2D.Double(200, 400), "A"));
        graph.addNode(new NodePlus(new Point2D.Double(300, 600), "B"));
        graph.addNode(new NodePlus(new Point2D.Double(500, 600), "C"));
        graph.addNode(new NodePlus(new Point2D.Double(400, 400), "D"));
        graph.addNode(new NodePlus(new Point2D.Double(600, 400), "E"));
        graph.addNode(new NodePlus(new Point2D.Double(400, 200), "F"));

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

        return graph;
    }

    /**
     * There are four available command line argument configurations:
     * 1. "" - Empty configuration launches default GUI app with empty graph.
     * 2. "-GUI path_to_file.sv" - Launches GUI app, loading graph from "path_to_file.sv".
     * 3. "-noGUI path_to_input_file.sv path_to_output_file.sv" - Launches console algorithm, getting graph from "path_to_input_file.sv" and loading result "to path_to_output_file.sv".
     * 4. "-noGUI path_to_input_file.sv path_to_output_file.sv log_level" - the same as above, setting algorithm log level to one of four: NO_LOG, CONSOLE, FILE, GUI.
     */
    public static void main(String[] args) {
        Graph g = prepareInput();
        Filer.saveGraphToFile(g, "graph.sv", new Filer.OnPerformed() {
            @Override
            public void onFinished(Exception reason) {
                Filer.loadGraphFromFile("graph.sv", new Filer.OnGraphLoaded() {
                    @Override
                    public void onFinished(Graph g, Exception reason) {
                        System.out.println(g);
                    }
                });
            }
        });

        if (args.length == 0) {
            launchGUI("");
        } else if (args[0].equals("-noGUI")) {
            Settings.setup(Log.Level.NO_LOG);
            if (args.length == 4) {
                String pathToInputFile = args[1];
                String pathToOutputFile = args[2];
                Log.Level level = Log.Level.valueOf(args[3]);
                // TODO: launch algorithm.
            } else if (args.length == 3) {
                String pathToInputFile = args[1];
                String pathToOutputFile = args[2];
                Log.Level level = Log.Level.CONSOLE;
                // TODO: launch algorithm.
            } else Log.cui().file(null).say("Wrong arguments provided! Aborting execution.");
        } else if (args[0].equals("-GUI")) {
            if (args.length == 2) {
                Settings.setup(Log.Level.GUI);
                launchGUI(args[1]);
            } else {
                Settings.setup(Log.Level.NO_LOG);
                Log.cui().file(null).say("Wrong arguments provided! Aborting execution.");
            }
        } else {
            Settings.setup(Log.Level.NO_LOG);
            Log.cui().file(null).say("Wrong arguments provided! Aborting execution.");
        }
    }

    private static void launchGUI(String saveFile) {
        Log.cui().say("Uptime started");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        visual = new PrimaVisual(saveFile);
        Log.gui().col(Log.Colors.GREEN).say("GUI launched...");
        Settings.setup(Log.Level.GUI);

        JFrame f = new JFrame(Settings.getString("app_name"));
        f.setMinimumSize(new Dimension(Settings.getInt("default_screen_width"), Settings.getInt("default_screen_height")));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setContentPane(visual.getMainPanel());
        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);
    }

    public static PrimaVisual getVisual() {
        return visual;
    }
}

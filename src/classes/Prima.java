package classes;

import classes.algorithm.PrimaAlgorithm;
import classes.graph.Graph;
import classes.graph.NodePlus;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

public class Prima {
    private static final String CONSOLE_ARG_GUI = "-GUI";
    private static final String CONSOLE_ARG_NO_GUI = "-noGUI";

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
     * 2. "-GUI path_to_file" - Launches GUI app, loading graph from "path_to_file.sv".
     * 3. "-noGUI path_to_input_file.sv path_to_output_file" - Launches console algorithm, getting graph from "path_to_input_file" and loading result "to path_to_output_file".
     * 4. "-noGUI path_to_input_file.sv path_to_output_file log_level" - the same as above, setting algorithm log level to one of four: NO_LOG, CONSOLE, FILE, GUI.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            launchGUI("");
        } else if (args[0].equals(CONSOLE_ARG_NO_GUI)) {
            Settings.setup(Log.Level.NO_LOG);
            if (args.length == 4) launchNoGUI(args[1], args[2], Log.Level.valueOf(args[3]));
            else if (args.length == 3) launchNoGUI(args[1], args[2], Log.Level.CONSOLE);
            else Log.cui().file(null).say("Wrong arguments provided, aborting execution");
        } else if (args[0].equals(CONSOLE_ARG_GUI)) {
            if (args.length == 2) {
                Settings.setup(Log.Level.GUI);
                launchGUI(args[1]);
            } else {
                Settings.setup(Log.Level.NO_LOG);
                Log.cui().file(null).say("Wrong arguments provided, aborting execution");
            }
        } else {
            Settings.setup(Log.Level.NO_LOG);
            Log.cui().file(null).say("Wrong arguments provided, aborting execution");
        }
    }

    private static void launchNoGUI(String loadFile, String saveFile, Log.Level logLevel) {
        Filer.loadGraphFromFile(loadFile, false, (graph, reason) -> {
            if (reason == null) {
                PrimaAlgorithm alg = new PrimaAlgorithm(logLevel);
                alg.solve(graph);
                Filer.saveGraphToFile(graph, saveFile, reason1 -> {
                    if (reason1 != null){
                        Log.consumeException("Сохранение невозможно", reason1);
                    }
                });
            } else {
                Log.consumeException("Файл не найден или содержимое файла повреждено", reason);
            }
        });
    }

    private static void launchGUI(String saveFile) {
        Log.cui().say("Uptime started!");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        visual = new PrimaVisual(saveFile);
        Log.gui().col(Log.Colors.GREEN).say("GUI launched!");
        Settings.setup(Log.Level.GUI);

        JFrame f = new JFrame(Settings.getString("app_name") + " - " + saveFile);
        f.setMinimumSize(new Dimension(Settings.getInt("default_screen_width"), Settings.getInt("default_screen_height")));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setContentPane(visual.getMainPanel());
        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                if (Settings.checkPref(Settings.userPath)) visual.preserve(true);
            }
        });
    }

    public static PrimaVisual getVisual() {
        return visual;
    }
}

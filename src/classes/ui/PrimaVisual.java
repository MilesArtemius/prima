package classes.ui;

import classes.Prima;
import classes.shapes.GraphShape;

import javax.swing.*;
import java.awt.*;

public class PrimaVisual {
    private JPanel root;
    private JButton test;
    private JButton launch;
    private JButton backward;
    private JButton forward;
    private JPanel contentPanel;
    private JPanel visualizationPanel;
    private JPanel graphShapePanel;
    private JPanel logsPanel;
    private JPanel runtimePanel;
    private JPanel launchPanel;
    private JPanel stepPanel;
    private JLabel logsTitle;
    private JLabel visualizationText;
    private JLabel logs;
    private JMenuBar menuBar1;
    private JMenu fileMenu;
    private JMenu settingsMenu;
    private JMenu helpMenu;
    private JPanel menuSeparator;
    private JLabel menuText;

    private GraphShape graph;

    public PrimaVisual() {
        graph = new GraphShape();
        graph.setGraph(Prima.prepareInput());
        graphShapePanel.add(graph, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
                GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        graphShapePanel.setBackground(Color.RED);

        fileMenu.setText("File");
        fileMenu.add(new JMenuItem("New...")); // Add graph presets.
        fileMenu.add(new JMenuItem("Save as..."));
        fileMenu.add(new JMenuItem("Save..."));
        fileMenu.add(new JMenuItem("Preserve")); // Save to Settings.getFileRoot if exists.

        settingsMenu.setText("Settings");
        settingsMenu.add(new JMenuItem("Set parameter"));
        settingsMenu.add(new JMenuItem("Change localization"));

        helpMenu.setText("Help");
        helpMenu.add(new JMenuItem("About app"));
        helpMenu.add(new JMenuItem("About us"));

        menuText.setText("Thanks for using our wonderful app!");
        visualizationText.setText("Graph:");
        logsTitle.setText("Logs:");

        launch.setText("Launch!");
        forward.setText("Step forward");
        backward.setText("Step backward");
        test.setText("Run tests");
    }

    public JPanel getMainPanel() {
        return root;
    }
}

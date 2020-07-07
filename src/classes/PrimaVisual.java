package classes;

import classes.dial.ParameterChangeDialog;
import classes.graph.Graph;
import classes.shapes.GraphShape;
import test.PrimaTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PrimaVisual {
    private JFrame parent;

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

    private JMenuItem newGraph;
    private JMenuItem saveGraphAs;
    private JMenuItem saveGraph;
    private JMenuItem preserveGraph;

    private JMenuItem setParameter;
    private JMenuItem changeLocalization;
    private JMenuItem setFilePath;
    private JMenuItem clearFilePath;

    private JMenuItem aboutApp;
    private JMenuItem aboutUs;

    public PrimaVisual(JFrame parent) {
        this.parent = parent;

        graph = new GraphShape();
        graph.setGraph(Prima.prepareInput());
        graphShapePanel.add(graph, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
                GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        logs.setText("<html>");

        newGraph = new JMenuItem();
        newGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.setGraph(new Graph());
                graph.repaint();
            }
        });
        saveGraphAs = new JMenuItem();
        saveGraphAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement;
            }
        });
        saveGraph = new JMenuItem();
        saveGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement;
            }
        });
        preserveGraph = new JMenuItem();
        preserveGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement;
            }
        });

        fileMenu.add(newGraph); // Add graph presets.
        fileMenu.add(saveGraphAs);
        fileMenu.add(saveGraph);
        fileMenu.add(preserveGraph); // Save to Settings.getFileRoot if exists.

        setParameter = new JMenuItem();
        setParameter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParameterChangeDialog pcd = new ParameterChangeDialog(graph);
                pcd.pack();
                pcd.setLocationRelativeTo(root);
                pcd.setVisible(true);
            }
        });
        changeLocalization = new JMenuItem();
        changeLocalization.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                resetAllNames();
            }
        });
        setFilePath = new JMenuItem();
        setFilePath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setDialogTitle("Choose user directory");
                fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileDialog.setAcceptAllFileFilterUsed(false);

                int status = fileDialog.showOpenDialog(null);
                if (status == JFileChooser.APPROVE_OPTION) {
                    System.out.println(fileDialog.getSelectedFile().getAbsolutePath());
                    Settings.alterUserPath(fileDialog.getSelectedFile().getAbsolutePath());
                    reEnableAll();
                } else if (status == JFileChooser.CANCEL_OPTION) {
                    System.out.println("User directory not set!");
                }
            }
        });
        clearFilePath = new JMenuItem();
        clearFilePath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.removeUserPath();
                reEnableAll();
            }
        });

        settingsMenu.add(setParameter);
        settingsMenu.add(changeLocalization);
        settingsMenu.add(setFilePath);
        settingsMenu.add(clearFilePath);

        aboutApp = new JMenuItem();
        aboutApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement;
            }
        });
        aboutUs = new JMenuItem();
        aboutUs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement;
            }
        });

        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimaTest.runTests();
            }
        });

        helpMenu.add(aboutApp);
        helpMenu.add(aboutUs);

        reEnableAll();
        resetAllNames();
    }

    private void reEnableAll() {
        preserveGraph.setEnabled(Settings.isUserPathSet());
        clearFilePath.setEnabled(Settings.isUserPathSet());
    }

    private void resetAllNames() {
        parent.setTitle(Settings.getString("app_name"));

        fileMenu.setText("File");
        newGraph.setText("New...");
        saveGraphAs.setText("Save as...");
        saveGraph.setText("Save");
        preserveGraph.setText("Preserve");

        settingsMenu.setText("Settings");
        setParameter.setText("Set parameter");
        changeLocalization.setText("Change localization");
        setFilePath.setText("Set default file path");
        clearFilePath.setText("Clear default file path");

        helpMenu.setText("Help");
        aboutApp.setText("About app");
        aboutUs.setText("About us");

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

    public void appendTextToLog(String text, List<String> attributes) {
        StringBuilder log = new StringBuilder(logs.getText());
        for (String attr: attributes) {
            log.append('<').append(attr).append('>');
        }
        log.append(text);
        Collections.reverse(attributes);
        for (String attr: attributes) {
            log.append("</").append(attr).append('>');
        }

        logs.setText(log.toString());
    }
}

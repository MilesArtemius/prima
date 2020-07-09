package classes;

import classes.algorithm.PrimaAlgorithm;
import classes.dial.ParameterChangeDialog;
import classes.graph.Graph;
import classes.shapes.GraphShape;
import test.PrimaTest;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu settingsMenu;
    private JMenu helpMenu;
    private JPanel menuSeparator;
    private JLabel menuText;
    private JScrollPane textScroll;
    private JButton reset;

    private PrimaAlgorithm algorithm;
    private GraphShape graph;
    private String openedFileName;

    private JMenuItem newGraph; // TODO: add submenu: load, choose from samples.
    private JMenuItem openGraph;
    private JMenuItem saveGraphAs;
    private JMenuItem saveGraph;
    private JMenuItem preserveGraph;

    private JMenuItem setParameter;
    private JMenu changeLocalization;
    private JMenuItem userLocale;
    private JMenuItem englishLocale;
    private JMenuItem russianLocale;
    private JMenuItem germanLocale;
    private JMenuItem latinLocale;
    private JMenuItem setFilePath;
    private JMenuItem addUserLocale;
    private JMenu clearFilePath;
    private JMenuItem clearAll;
    private JMenuItem clearConstants;
    private JMenuItem clearDictionary;

    private JMenuItem aboutApp;
    private JMenuItem aboutUs;

    public PrimaVisual(String fileName) {
        this.openedFileName = fileName;

        logs.setText("<html>");

        graph = new GraphShape();
        Filer.OnGraphLoaded loadListner = (loadedGraph, reason) -> {
            if (reason != null) {
                Log.consumeException("Файл не найден или содержимое файла повреждено", reason);
                graph.setGraph(new Graph());
            } else {
                graph.setGraph(loadedGraph);
            }
        };
        if (!openedFileName.equals("")) Filer.loadGraphFromFile(openedFileName, true, loadListner);
        else if (!Settings.getPref(Settings.preservedGraph).equals("")) Filer.loadGraphFromFile(Settings.getPref(Settings.preservedGraph), true, loadListner);
        else graph.setGraph(Prima.prepareInput());

        graphShapePanel.add(graph, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
                GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        algorithm = new PrimaAlgorithm();

        initFileMenu();
        initSettingsMenu();
        initAboutMenu();

        saveGraph.setEnabled(openedFileName.equals(""));

        reEnableAll();
        resetAllNames();

        initButtons();
    }

    private void initFileMenu() {
        newGraph = new JMenuItem();
        newGraph.addActionListener(e -> {
            saveGraph.setEnabled(false);
            graph.setGraph(new Graph());
            algorithm = new PrimaAlgorithm();
            graph.repaint();
        });
        openGraph = new JMenuItem();
        openGraph.addActionListener(e -> {
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.setDialogTitle("Choose graph file");
            fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
            fileDialog.setFileFilter(new FileNameExtensionFilter("GRAPH FILES", Filer.GRAPH_FILE_EXTENSION));
            fileDialog.setAcceptAllFileFilterUsed(false);

            int status = fileDialog.showOpenDialog(root);
            if (status == JFileChooser.APPROVE_OPTION) {
                System.out.println(fileDialog.getSelectedFile().getAbsolutePath());
                PrimaVisual.this.openedFileName = fileDialog.getSelectedFile().getName();
                PrimaVisual.this.saveGraph.setEnabled(true);

                Filer.loadGraphFromFile(fileDialog.getSelectedFile().getAbsolutePath(), true, (graph, reason) -> {
                    if (reason != null) {
                        Log.consumeException("Файл не найден или содержимое файла повреждено", reason);
                    } else {
                        PrimaVisual.this.graph.setGraph(graph);
                        PrimaVisual.this.graph.repaint();
                    }
                });
            } else if (status == JFileChooser.CANCEL_OPTION) {
                System.out.println("Graph file not chosen!");
            }
        });
        saveGraphAs = new JMenuItem();
        saveGraphAs.addActionListener(e -> {
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.setDialogTitle("Save graph file as");
            fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileDialog.setDialogType(JFileChooser.SAVE_DIALOG);
            fileDialog.setFileFilter(new FileNameExtensionFilter("GRAPH FILES", Filer.GRAPH_FILE_EXTENSION));
            fileDialog.setAcceptAllFileFilterUsed(false);

            int status = fileDialog.showOpenDialog(root);
            if (status == JFileChooser.APPROVE_OPTION) {
                System.out.println(fileDialog.getSelectedFile().getAbsolutePath());
                PrimaVisual.this.openedFileName = fileDialog.getSelectedFile().getName();
                PrimaVisual.this.saveGraph.setEnabled(true);

                Filer.saveGraphToFile(PrimaVisual.this.graph.getGraph(), fileDialog.getSelectedFile().getAbsolutePath(), reason -> {
                    if (reason == null) {
                        Log.gui(Log.Attributes.BOLD).col(Log.Colors.GREEN).say("Graph saved!");
                    } else {
                        Log.consumeException("Файл не может быть сохранён", reason);
                    }
                });
            } else if (status == JFileChooser.CANCEL_OPTION) {
                System.out.println("Graph file not chosen!");
            }
        });
        saveGraph = new JMenuItem();
        saveGraph.addActionListener(e -> Filer.saveGraphToFile(graph.getGraph(), openedFileName, reason -> {
            if (reason == null) {
                Log.gui(Log.Attributes.BOLD).col(Log.Colors.GREEN).say("Graph saved!");
            } else {
                Log.consumeException("Файл не может быть сохранён", reason);
            }
        }));
        preserveGraph = new JMenuItem();
        preserveGraph.addActionListener(e -> preserve(false));

        fileMenu.add(newGraph);
        fileMenu.add(openGraph);
        fileMenu.add(saveGraphAs);
        fileMenu.add(saveGraph);
        fileMenu.add(preserveGraph);
    }

    private void initSettingsMenu() {
        setParameter = new JMenuItem();
        setParameter.addActionListener(e -> {
            ParameterChangeDialog pcd = new ParameterChangeDialog(graph, SwingUtilities.getWindowAncestor(root), "Reset setting");
            pcd.pack();
            pcd.setLocationRelativeTo(root);
            pcd.setVisible(true);
        });
        changeLocalization = new JMenu();
        userLocale = new JMenuItem();
        userLocale.addActionListener(e -> Settings.changeLocalization(Settings.Locales.USER, this::resetAllNames));
        englishLocale = new JMenuItem();
        englishLocale.addActionListener(e -> Settings.changeLocalization(Settings.Locales.ENGLISH, this::resetAllNames));
        russianLocale = new JMenuItem();
        russianLocale.addActionListener(e -> Settings.changeLocalization(Settings.Locales.RUSSIAN, this::resetAllNames));
        germanLocale = new JMenuItem();
        germanLocale.addActionListener(e -> Settings.changeLocalization(Settings.Locales.GERMAN, this::resetAllNames));
        latinLocale = new JMenuItem();
        latinLocale.addActionListener(e -> Settings.changeLocalization(Settings.Locales.LATIN, this::resetAllNames));
        changeLocalization.add(userLocale);
        changeLocalization.add(englishLocale);
        changeLocalization.add(russianLocale);
        changeLocalization.add(germanLocale);
        changeLocalization.add(latinLocale);
        setFilePath = new JMenuItem();
        setFilePath.addActionListener(e -> {
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.setDialogTitle("Set directory");
            fileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
            fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileDialog.setAcceptAllFileFilterUsed(false);

            int status = fileDialog.showOpenDialog(root);
            if (status == JFileChooser.APPROVE_OPTION) {
                System.out.println(fileDialog.getSelectedFile().getAbsolutePath());
                Settings.alterUserPath(fileDialog.getSelectedFile().getAbsolutePath(), this::reEnableAll);
            } else if (status == JFileChooser.CANCEL_OPTION) {
                System.out.println("User directory not chosen!");
            }
        });
        addUserLocale = new JMenuItem();
        addUserLocale.addActionListener(e -> {
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.setDialogTitle("Choose localization file");
            fileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
            fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileDialog.setFileFilter(new FileNameExtensionFilter("PROPERTIES FILES", Filer.PROPERTIES_FILE_EXTENSION));
            fileDialog.setAcceptAllFileFilterUsed(false);

            int status = fileDialog.showOpenDialog(root);
            if (status == JFileChooser.APPROVE_OPTION) {
                System.out.println(fileDialog.getSelectedFile().getAbsolutePath());
                Settings.alterLocalization(fileDialog.getSelectedFile().getAbsolutePath(), this::resetAllNames);
            } else if (status == JFileChooser.CANCEL_OPTION) {
                System.out.println("Localization file not chosen!");
            }
        });
        clearFilePath = new JMenu();
        clearAll = new JMenuItem();
        clearAll.addActionListener(e -> Settings.removeUserPath(() -> {
            reEnableAll();
            resetAllNames();
            graph.repaint();
        }));
        clearConstants = new JMenuItem();
        clearConstants.addActionListener(e -> Settings.resetConstants(() -> graph.repaint()));
        clearDictionary = new JMenuItem();
        clearDictionary.addActionListener(e -> Settings.resetDictionary(this::resetAllNames));
        clearFilePath.add(clearAll);
        clearFilePath.add(clearConstants);
        clearFilePath.add(clearDictionary);

        settingsMenu.add(setParameter);
        settingsMenu.add(changeLocalization);
        settingsMenu.add(setFilePath);
        settingsMenu.add(addUserLocale);
        settingsMenu.add(clearFilePath);
    }

    private void initAboutMenu() {
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

        helpMenu.add(aboutApp);
        helpMenu.add(aboutUs);
    }

    private void initButtons() {
        test.addActionListener(e -> PrimaTest.runTests());

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.getGraph().reset();
                graph.repaint();
            }
        });

        launch.addActionListener(e -> algorithm.threadSolveAll(graph.getGraph(), () -> graph.repaint(), null));

        forward.addActionListener(e -> algorithm.threadSolveStep(graph.getGraph(), () -> graph.repaint(), null));

        backward.addActionListener(e -> {
            algorithm.stepBack();
            graph.repaint();
        });
    }



    public void preserve(boolean isFinal) {
        String date = (new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")).format(Calendar.getInstance().getTime());
        openedFileName = Settings.getPref(Settings.userPath) + File.separator + Settings.userPathDir + File.separator + date + "_graph";
        Settings.setPref(Settings.preservedGraph, openedFileName);
        Filer.OnPerformed listener = reason -> {
            if (reason == null) {
                Log.gui(Log.Attributes.BOLD).col(Log.Colors.GREEN).say("Graph saved as " + openedFileName + "!");
            } else {
                Log.consumeException("Файл не может быть сохранён", reason);
            }
        };
        if (!isFinal) Filer.saveGraphToFile(graph.getGraph(), openedFileName, listener);
        else Filer.saveGraphToFileNoThread(graph.getGraph(), openedFileName, listener);
    }

    public Dimension getGraphShapeDimension() {
        return graph.getSize();
    }



    private void reEnableAll() {
        boolean enabled = Settings.checkPref(Settings.userPath);
        preserveGraph.setEnabled(enabled);
        addUserLocale.setEnabled(enabled);
        clearFilePath.setEnabled(enabled);
        setFilePath.setEnabled(!enabled);
    }

    private void resetAllNames() {
        if (SwingUtilities.getWindowAncestor(root) != null)
            ((JFrame) SwingUtilities.getWindowAncestor(root)).setTitle(Settings.getString("app_name"));

        fileMenu.setText("File");
        openGraph.setText("Open...");
        newGraph.setText("New...");
        saveGraphAs.setText("Save as...");
        saveGraph.setText("Save");
        preserveGraph.setText("Preserve");

        settingsMenu.setText("Settings");
        setParameter.setText("Set parameter");
        changeLocalization.setText("Change localization");
        userLocale.setText(Settings.getString("user_defined_localization_name"));
        englishLocale.setText(Settings.Locales.ENGLISH.getLocale());
        russianLocale.setText(Settings.Locales.RUSSIAN.getLocale());
        germanLocale.setText(Settings.Locales.GERMAN.getLocale());
        latinLocale.setText(Settings.Locales.LATIN.getLocale());
        setFilePath.setText("Set default file path");
        addUserLocale.setText("Add user-defined localization");
        clearFilePath.setText("Clear default file path");
        clearAll.setText("Clear everything");
        clearConstants.setText("Clear user-defined constants");
        clearDictionary.setText("Clear user-defined localization");

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
        reset.setText("Reset graph");
    }

    public JPanel getMainPanel() {
        return root;
    }

    public void appendTextToLog(String text, String color, List<String> attributes) {
        StringBuilder log = new StringBuilder(logs.getText());
        for (String attr: attributes) {
            log.append('<').append(attr).append('>');
        }
        if (!color.equals("")) log.append("<font color=\"").append(color).append("\">");
        log.append(text);
        if (!color.equals("")) log.append("</font>");
        Collections.reverse(attributes);
        for (String attr: attributes) {
            log.append("</").append(attr).append('>');
        }

        logs.setText(log.toString());
    }
}

package classes;

import classes.dial.ParameterChangeDialog;
import classes.graph.Graph;
import classes.shapes.GraphShape;
import test.PrimaTest;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

    public PrimaVisual() {
        graph = new GraphShape();
        graph.setGraph(Prima.prepareInput());
        graphShapePanel.add(graph, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
                GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        logs.setText("<html>");

        initFileMenu();
        initSettingsMenu();
        initAboutMenu();

        reEnableAll();
        resetAllNames();
    }

    private void initFileMenu() {
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
    }

    private void initSettingsMenu() {
        setParameter = new JMenuItem();
        setParameter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParameterChangeDialog pcd = new ParameterChangeDialog(graph, SwingUtilities.getWindowAncestor(root), "Reset setting");
                pcd.pack();
                pcd.setLocationRelativeTo(root);
                pcd.setVisible(true);
            }
        });
        changeLocalization = new JMenu();
        userLocale = new JMenuItem();
        userLocale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.changeLocalization(Settings.Locales.USER, new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        resetAllNames();
                    }
                });
            }
        });
        englishLocale = new JMenuItem();
        englishLocale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.changeLocalization(Settings.Locales.ENGLISH, new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        resetAllNames();
                    }
                });
            }
        });
        russianLocale = new JMenuItem();
        russianLocale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.changeLocalization(Settings.Locales.RUSSIAN, new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        resetAllNames();
                    }
                });
            }
        });
        germanLocale = new JMenuItem();
        germanLocale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.changeLocalization(Settings.Locales.GERMAN, new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        resetAllNames();
                    }
                });
            }
        });
        latinLocale = new JMenuItem();
        latinLocale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.changeLocalization(Settings.Locales.LATIN, new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        resetAllNames();
                    }
                });
            }
        });
        changeLocalization.add(userLocale);
        changeLocalization.add(englishLocale);
        changeLocalization.add(russianLocale);
        changeLocalization.add(germanLocale);
        changeLocalization.add(latinLocale);
        setFilePath = new JMenuItem();
        setFilePath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setDialogTitle("Choose user directory");
                fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileDialog.setAcceptAllFileFilterUsed(false);

                int status = fileDialog.showOpenDialog(root);
                if (status == JFileChooser.APPROVE_OPTION) {
                    System.out.println(fileDialog.getSelectedFile().getAbsolutePath());
                    Settings.alterUserPath(fileDialog.getSelectedFile().getAbsolutePath(), new Settings.OnLongActionFinished() {
                        @Override
                        public void onFinished() {
                            reEnableAll();
                        }
                    });
                } else if (status == JFileChooser.CANCEL_OPTION) {
                    System.out.println("User directory not chosen!");
                }
            }
        });
        addUserLocale = new JMenuItem();
        addUserLocale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setDialogTitle("Choose localization file");
                fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileDialog.setFileFilter(new FileNameExtensionFilter("PROPERTIES FILES", "properties"));
                fileDialog.setAcceptAllFileFilterUsed(false);

                int status = fileDialog.showOpenDialog(root);
                if (status == JFileChooser.APPROVE_OPTION) {
                    System.out.println(fileDialog.getSelectedFile().getAbsolutePath());
                    Settings.alterLocalization(fileDialog.getSelectedFile().getAbsolutePath(), new Settings.OnLongActionFinished() {
                        @Override
                        public void onFinished() {
                            resetAllNames();
                        }
                    });
                } else if (status == JFileChooser.CANCEL_OPTION) {
                    System.out.println("Localization file not chosen!");
                }
            }
        });
        clearFilePath = new JMenu();
        clearAll = new JMenuItem();
        clearAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.removeUserPath(new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        reEnableAll();
                        resetAllNames();
                    }
                });
            }
        });
        clearConstants = new JMenuItem();
        clearConstants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.resetConstants(new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        graph.repaint();
                    }
                });
            }
        });
        clearDictionary = new JMenuItem();
        clearDictionary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.resetDictionary(new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        resetAllNames();
                    }
                });
            }
        });
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

        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimaTest.runTests();
            }
        });

        helpMenu.add(aboutApp);
        helpMenu.add(aboutUs);
    }



    private void reEnableAll() {
        boolean enabled = Settings.checkPref(Settings.userPath);
        preserveGraph.setEnabled(enabled);
        addUserLocale.setEnabled(enabled);
        clearFilePath.setEnabled(enabled);
    }

    private void resetAllNames() {
        if (SwingUtilities.getWindowAncestor(root) != null)
            ((JFrame) SwingUtilities.getWindowAncestor(root)).setTitle(Settings.getString("app_name"));

        fileMenu.setText("File");
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

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
    private JPanel toolbar;
    private JButton button1;
    private JComboBox comboBox1;

    private GraphShape graph;

    public PrimaVisual() {
        graph = new GraphShape();
        graph.setGraph(Prima.prepareInput());
        graphShapePanel.add(graph, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
                GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        graphShapePanel.setBackground(Color.RED);
    }

    public JPanel getMainPanel() {
        return root;
    }
}

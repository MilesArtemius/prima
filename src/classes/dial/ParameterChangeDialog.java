package classes.dial;

import classes.Settings;
import classes.shapes.GraphShape;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ParameterChangeDialog extends JDialog {
    private JPanel contentPane;
    private JTextField input;
    private JButton apply;
    private JPanel inputPanel;
    private JScrollPane scrollText;
    private JLabel text;
    private JSpinner parameterSpinner;

    HashMap<String, String> content;

    public ParameterChangeDialog(GraphShape graph, Window owner, String title) {
        super(owner, title);

        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        content = Settings.getConstantsDescription();

        text.setText("<html>");
        for (Map.Entry<String, String> entry: content.entrySet()) addEntry(entry.getKey(), entry.getValue(), "");

        apply.setText("Apply");

        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                String up = input.getText();
                text.setText("<html>");
                for (Map.Entry<String, String> entry: content.entrySet()) if (entry.getKey().contains(up)) addEntry(entry.getKey(), entry.getValue(), up);
            }
        });

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parameterSpinner.validate();
                if (!input.getText().equals("")) Settings.alterParameter(input.getText(), (int) parameterSpinner.getValue(), new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
                        if (!Settings.checkPref(Settings.userPath)) System.out.println("Warning! Path for parameter storing not found, parameter changed until session end!");
                        graph.repaint();
                        dispose();
                    }
                });
            }
        });
    }

    public void addEntry(String key, String value, String containment) {
        String txt = text.getText();
        int position = key.indexOf(containment);
        String keyStart = key.substring(0, position), keyEnd = key.substring(position + containment.length());
        txt += "<p><code>" + keyStart + "<b>" + containment + "</b>" + keyEnd + "</code> - " + value + "</p><br>";
        text.setText(txt);
    }
}

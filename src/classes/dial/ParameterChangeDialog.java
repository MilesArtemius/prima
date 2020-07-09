package classes.dial;

import classes.Settings;
import classes.shapes.GraphShape;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
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
        getRootPane().setDefaultButton(apply);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        content = Settings.getConstantsDescription();

        input.setToolTipText(Settings.getString("reset_setting_dialog_prompt"));

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
        input.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        input.addKeyListener(new KeyAdapter() {
            public void keyPressed (java.awt.event.KeyEvent evt){
                if (evt.getKeyCode() == KeyEvent.VK_TAB) {
                    String up = input.getText();
                    for (Map.Entry<String, String> entry: content.entrySet()) if (entry.getKey().contains(up)) {
                        input.setText(entry.getKey());
                        parameterSpinner.grabFocus();
                        break;
                    }
                }
            }
        });

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parameterSpinner.validate();
                if (!input.getText().equals("")) Settings.alterParameter(input.getText(), (int) parameterSpinner.getValue(), new Settings.OnLongActionFinished() {
                    @Override
                    public void onFinished() {
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

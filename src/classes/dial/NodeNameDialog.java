package classes.dial;

import classes.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodeNameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField textField1;
    private ActionListener listener;

    public NodeNameDialog(Window owner, String title) {
        super(owner, title);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        textField1.setToolTipText(Settings.getString("create_node_dialog_prompt"));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listener != null) listener.actionPerformed(e);
            }
        });
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public String getResult() {
        return textField1.getText();
    }
}

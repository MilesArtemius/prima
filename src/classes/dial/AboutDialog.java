package classes.dial;

import classes.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JPanel buttonPanel;
    private JScrollPane textPanel;
    private JLabel text;

    public AboutDialog(Window owner, String HTML) {
        super(owner);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buttonCancel.setText(Settings.getString("about_dialog_dismiss"));
        text.setText(HTML);

        buttonCancel.addActionListener(e -> dispose());
    }
}

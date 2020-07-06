package classes.dial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class ArkWeightDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JSpinner weightSpinner;
    private JPanel contentPanel;
    private ActionListener listener;

    public ArkWeightDialog(Window owner, String title) {
        super(owner, title);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        weightSpinner.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listener != null) listener.actionPerformed(e);
            }
        });
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public int getResult() throws ParseException {
        weightSpinner.commitEdit();
        return (Integer) weightSpinner.getValue();
    }
}

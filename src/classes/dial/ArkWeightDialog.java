package classes.dial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class ArkWeightDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JSpinner spinner1;
    private ActionListener listener;

    public ArkWeightDialog(Window owner, String title) {
        super(owner, title);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        spinner1.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));

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
        spinner1.commitEdit();
        return (Integer) spinner1.getValue();
    }
}

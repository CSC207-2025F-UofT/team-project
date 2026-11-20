package view;

import javax.swing.*;
import java.awt.*;

public class PinPanel extends JPanel {
    public PinPanel(Navigator nav) {
        setLayout(new BorderLayout());
        add(new HeaderBar(nav, ScreenNames.MAIN, "Caregiver Mode"), BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(24, 48, 24, 48));

        JLabel acornImage = new JLabel(Icons.acorn(160), SwingConstants.CENTER);
        acornImage.setAlignmentX(CENTER_ALIGNMENT);

        JLabel pinLabel = new JLabel("PIN");
        pinLabel.setAlignmentX(CENTER_ALIGNMENT);

        JTextField pinField = new JTextField(10);
        pinField.setMaximumSize(new Dimension(200, 30));

        JButton submit = new JButton("Submit");
        submit.setAlignmentX(CENTER_ALIGNMENT);
        // TODO later: validate a 4-digit PIN before continuing
        submit.addActionListener(e -> {
            pinField.setText("");
            nav.show(ScreenNames.CAREGIVER_MENU);
        });

        form.add(acornImage);
        form.add(Box.createVerticalStrut(24));
        form.add(pinLabel);
        form.add(pinField);
        form.add(Box.createVerticalStrut(16));
        form.add(submit);
        add(form, BorderLayout.CENTER);
    }
}
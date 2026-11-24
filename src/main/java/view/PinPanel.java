package view;

import javax.swing.*;
import java.awt.*;

public class PinPanel extends JPanel {
    public PinPanel(Navigator nav) {
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Large back button so a child can leave quickly (TEMPORARY - can change dimensions later)
        JButton back = new JButton("BACK", Icons.acorn(160));
        back.setHorizontalTextPosition(SwingConstants.CENTER);
        back.setVerticalTextPosition(SwingConstants.BOTTOM);
        back.setFont(back.getFont().deriveFont(Font.BOLD, 28f));
        back.setPreferredSize(new Dimension(0, 260));
        back.addActionListener(e -> nav.show(ScreenNames.MAIN));
        add(back, BorderLayout.CENTER);

        // PIN entry (no validation yet)
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel pinLabel = new JLabel("PIN");
        pinLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPasswordField pinField = new JPasswordField(10);
        pinField.setMaximumSize(new Dimension(200, 30));

        JButton submit = new JButton("Submit");
        submit.setAlignmentX(CENTER_ALIGNMENT);
        // TODO Phase 2: validate a 4-digit PIN before continuing
        submit.addActionListener(e -> {
            pinField.setText("");
            nav.show(ScreenNames.CAREGIVER_MENU);
        });

        form.add(pinLabel);
        form.add(Box.createVerticalStrut(4));
        form.add(pinField);
        form.add(Box.createVerticalStrut(12));
        form.add(submit);
        add(form, BorderLayout.SOUTH);
    }
}
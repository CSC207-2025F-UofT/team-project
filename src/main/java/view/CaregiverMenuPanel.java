package view;

import javax.swing.*;
import java.awt.*;

public class CaregiverMenuPanel extends JPanel {
    public CaregiverMenuPanel(Navigator nav) {
        setLayout(new BorderLayout());
        add(new HeaderBar(nav, ScreenNames.MAIN, "Caregiver Mode"), BorderLayout.NORTH);

        JPanel choices = new JPanel(new GridLayout(1, 2, 16, 16));
        choices.setBorder(BorderFactory.createEmptyBorder(48, 24, 48, 24));

        choices.add(createChoice("History", ScreenNames.HISTORY, nav));
        choices.add(createChoice("Customize", ScreenNames.CUSTOMIZE, nav));

        add(choices, BorderLayout.CENTER);
    }

    private JButton createChoice(String text, String target, Navigator nav) {
        JButton button = new JButton(text, Icons.acorn(120));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));
        button.addActionListener(e -> nav.show(target));
        return button;
    }
}
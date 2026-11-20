package view;

import javax.swing.*;
import java.awt.*;

public class HeaderBar extends JPanel {
    public HeaderBar(Navigator nav, String backTarget, String title) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton back = new JButton("Back", Icons.acorn(28));
        back.addActionListener(e -> nav.show(backTarget));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));

        add(back, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);
    }
}
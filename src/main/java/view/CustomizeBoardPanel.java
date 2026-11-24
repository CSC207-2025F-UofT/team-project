package view;

import javax.swing.*;
import java.awt.*;

public class CustomizeBoardPanel extends JPanel {
    public CustomizeBoardPanel(Navigator nav) {
        setLayout(new BorderLayout());
        add(new HeaderBar(nav, ScreenNames.CAREGIVER_MENU, "Customize Board"), BorderLayout.NORTH);
        add(new JLabel("Customize board (coming in next PR)", SwingConstants.CENTER),
                BorderLayout.CENTER);
    }
}
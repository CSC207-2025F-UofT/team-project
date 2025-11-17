package view;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Home Page (Placeholder)", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

package view;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#000000")); // Use a background color

        JLabel welcomeLabel = new JLabel("Welcome Home! (Design this panel here)");
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.decode("#1E90FF")); // Light Blue Text

        add(welcomeLabel);
    }
}
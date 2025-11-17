package view;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    private final JLabel welcomeLabel;
    private final JPanel mainContentPanel;

    public HomePanel() {
        // --- Color Palette ---
        Color BG_BLACK = Color.decode("#000000");
        Color PANEL_DARK = Color.decode("#020F28");
        Color TEXT_LIGHT = Color.decode("#E6E6E6");

        this.setLayout(new BorderLayout());
        this.setBackground(BG_BLACK);

        // Section 1 (North: Welcome Banner)
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(PANEL_DARK.brighter());
        welcomeLabel = new JLabel("Welcome Home! (Loading username...)");
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_LIGHT);
        welcomePanel.add(welcomeLabel);
        welcomePanel.setPreferredSize(new Dimension(0, 120)); // Give it a fixed height

        // Main Content Panel (Center: Grid layout for sections 2 & 3)
        mainContentPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        mainContentPanel.setBackground(BG_BLACK);

        this.add(welcomePanel, BorderLayout.NORTH);
        this.add(mainContentPanel, BorderLayout.CENTER);

        // Build the two sections (based on your layout)
        JPanel section2 = createDueSoonPanel(BG_BLACK, PANEL_DARK, TEXT_LIGHT);
        JPanel section3 = createPlaceholderPanel(BG_BLACK, PANEL_DARK, TEXT_LIGHT);

        // Add to main content panel with wrappers for padding
        JPanel wrapper2 = new JPanel(new BorderLayout());
        wrapper2.setBackground(BG_BLACK);
        wrapper2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        wrapper2.add(section2, BorderLayout.CENTER);

        JPanel wrapper3 = new JPanel(new BorderLayout());
        wrapper3.setBackground(BG_BLACK);
        wrapper3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        wrapper3.add(section3, BorderLayout.CENTER);

        mainContentPanel.add(wrapper2);
        mainContentPanel.add(wrapper3);
    }

    /**
     * Helper method to create the "Due Soon" section (Top Half).
     */
    private JPanel createDueSoonPanel(Color bgBlack, Color panelDark, Color textLight) {
        JPanel panel = new JPanel();
        panel.setBackground(panelDark);

        JLabel label = new JLabel("Due Soon:");
        label.setFont(new Font("Georgia", Font.BOLD, 20));
        label.setForeground(textLight);

        // Use BoxLayout for the main section2 layout
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(15, 0)));
        panel.add(Box.createHorizontalStrut(10)); // Add some space

        JPanel infoContainerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        infoContainerPanel.setOpaque(false);
        infoContainerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        for (int i = 0; i < 3; i++) {
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(Color.BLACK);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            JLabel infoLabel = new JLabel("Info " + (i+1), SwingConstants.CENTER);
            infoLabel.setForeground(Color.WHITE);
            infoLabel.setFont(new Font("Georgia", Font.PLAIN, 16));
            infoPanel.add(infoLabel, BorderLayout.CENTER);
            infoContainerPanel.add(infoPanel);
        }

        panel.add(infoContainerPanel);
        // Ensure the infoContainer panel takes up remaining space
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    /**
     * Helper method to create the placeholder section (Bottom Half).
     */
    private JPanel createPlaceholderPanel(Color bgBlack, Color panelDark, Color textLight) {
        JPanel panel = new JPanel(new GridBagLayout()); // Use GridBag to center the label
        panel.setBackground(panelDark);
        JLabel label = new JLabel("Courses/Grades Section Placeholder");
        label.setFont(new Font("Georgia", Font.BOLD, 20));
        label.setForeground(textLight);
        panel.add(label);
        return panel;
    }


    /**
     * Called by DashboardView to set the username in the welcome message.
     */
    public void setUsername(String username) {
        this.welcomeLabel.setText("Welcome, " + username + "!");
    }
}
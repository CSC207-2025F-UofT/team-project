package view;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Placeholder until each use case has its own screen.
public class BuildingView extends JPanel {
    public BuildingView(String name, String description, Runnable back) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel title = new JLabel(name, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, BorderLayout.NORTH);
        add(new JLabel(description, SwingConstants.CENTER), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Street");
        backButton.addActionListener(event -> back.run());
        add(backButton, BorderLayout.SOUTH);
    }
}

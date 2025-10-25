package main;
import javax.swing.*;
import java.awt.*;

public class MainScreen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Main Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLayout(new BorderLayout(10, 10));

            // ---------- Image ----------
            ImageIcon icon = new ImageIcon("path/to/your/image.png");
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            frame.add(imageLabel, BorderLayout.NORTH);

            // ---------- 2x2 Buttons ----------
            JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            buttonPanel.add(new JButton("Single Player"));
            buttonPanel.add(new JButton("Multiplayer"));
            buttonPanel.add(new JButton("Manage Study Set"));
            buttonPanel.add(new JButton("Leaderboard"));

            frame.add(buttonPanel, BorderLayout.CENTER);

            // ---------- Finish ----------
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

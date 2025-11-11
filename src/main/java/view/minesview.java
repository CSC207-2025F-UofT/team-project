package view;

import javax.swing.*;
import java.awt.*;

public class minesview {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new minesview().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Mini Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // ===== LEFT PANEL (controls) =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel walletLabel = new JLabel("Wallet:");
        JTextField walletField = new JTextField("$100.00");
        walletField.setEditable(false);

        JLabel betLabel = new JLabel("Bet amount:");
        JTextField betField = new JTextField("$20.00");
        JButton submitButton = new JButton("Submit");

        JPanel betPanel = new JPanel(new BorderLayout(5, 5));
        betPanel.add(betField, BorderLayout.CENTER);
        betPanel.add(submitButton, BorderLayout.EAST);

        JLabel multiplierLabel = new JLabel("Multiplier:");
        JSlider multiplierSlider = new JSlider(1, 5, 2);
        multiplierSlider.setMajorTickSpacing(1);
        multiplierSlider.setPaintTicks(true);
        multiplierSlider.setPaintLabels(true);

        JLabel potentialLabel = new JLabel("Potential Winnings:");
        JTextField potentialField = new JTextField("$40.00");
        potentialField.setEditable(false);

        JLabel winningsLabel = new JLabel("Winnings:");
        JTextField winningsField = new JTextField("$2.00");
        winningsField.setEditable(false);
        JButton cashoutButton = new JButton("Cashout");

        leftPanel.add(walletLabel);
        leftPanel.add(walletField);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(betLabel);
        leftPanel.add(betPanel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(multiplierLabel);
        leftPanel.add(multiplierSlider);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(potentialLabel);
        leftPanel.add(potentialField);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(winningsLabel);
        leftPanel.add(winningsField);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(cashoutButton);

        // ===== CENTER PANEL (game grid) =====
        JPanel gridPanel = new JPanel(new GridLayout(5, 5, 5, 5));
        for (int i = 0; i < 25; i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(70, 70));
            gridPanel.add(tile);
        }
        gridPanel.setBorder(BorderFactory.createTitledBorder("Minigame UI"));

        // ===== ADD TO FRAME =====
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(gridPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}

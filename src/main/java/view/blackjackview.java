package view;

import javax.swing.*;
import java.awt.*;

public class blackjackview {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new blackjackview().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Blackjack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
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
        leftPanel.add(potentialLabel);
        leftPanel.add(potentialField);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(winningsLabel);
        leftPanel.add(winningsField);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(cashoutButton);

        // ===== CENTER PANEL (Blackjack Table) =====
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Blackjack Table"));
        tablePanel.setBackground(new Color(0, 100, 0)); // green felt

        // Dealer area
        JPanel dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        dealerPanel.setOpaque(false);
        JLabel dealerLabel = new JLabel("Dealer's Hand:");
        dealerLabel.setForeground(Color.WHITE);
        dealerPanel.add(dealerLabel);

        // 2 card placeholders for dealer
        for (int i = 0; i < 2; i++) {
            dealerPanel.add(createCardPlaceholder());
        }

        // Player area
        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        playerPanel.setOpaque(false);
        JLabel playerLabel = new JLabel("Your Hand:");
        playerLabel.setForeground(Color.WHITE);
        playerPanel.add(playerLabel);

        // 2 card placeholders for player
        for (int i = 0; i < 2; i++) {
            playerPanel.add(createCardPlaceholder());
        }

        // Action buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        JButton dealButton = new JButton("Deal");
        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");
        JButton doubleButton = new JButton("Double Down");

        actionPanel.add(dealButton);
        actionPanel.add(hitButton);
        actionPanel.add(standButton);
        actionPanel.add(doubleButton);

        tablePanel.add(dealerPanel, BorderLayout.NORTH);
        tablePanel.add(playerPanel, BorderLayout.CENTER);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(tablePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JLabel createCardPlaceholder() {
        JLabel card = new JLabel("card", SwingConstants.CENTER); //🂠
        card.setPreferredSize(new Dimension(80, 120));
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        card.setForeground(Color.WHITE);
        card.setFont(new Font("SansSerif", Font.BOLD, 24));
        return card;
    }
}

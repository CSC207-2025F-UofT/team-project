package view;

import entity.User;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {
    public ProfileFrame(User user, JFrame mainMenu) {
        setTitle("Profile");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1));
        centerPanel.setFont(new Font("Arial", Font.PLAIN, 32));

        JLabel usernameLabel = new JLabel("Username: " + user.getUsername(), SwingConstants.CENTER);
        JLabel balanceLabel = new JLabel("Balance: $" + user.getBalance(), SwingConstants.CENTER);
        JLabel betLabel = new JLabel("Number of Bets: " + user.getTotalBets(), SwingConstants.CENTER);
        JLabel gamesLabel = new JLabel("Games Played: " + user.getGamesPlayed(), SwingConstants.CENTER);

        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        betLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        gamesLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        centerPanel.add(usernameLabel);
        centerPanel.add(balanceLabel);
        centerPanel.add(betLabel);
        centerPanel.add(gamesLabel);

        JButton returnBtn = new JButton("Return to Home");
        returnBtn.setFont(new Font("Arial", Font.BOLD, 35));

        add(centerPanel, BorderLayout.CENTER);
        add(returnBtn, BorderLayout.SOUTH);

        returnBtn.addActionListener(e -> {
            mainMenu.setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}

package view;

import interface_adapter.Profile.ProfileViewModel;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends JFrame {

    public ProfileFrame(ProfileViewModel vm, JFrame mainMenu) {

        setTitle("Profile");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(4, 1));

        centerPanel.add(new JLabel("Username: " + vm.getUsername(), SwingConstants.CENTER));
        centerPanel.add(new JLabel("Balance: $" + vm.getBalance(), SwingConstants.CENTER));
        centerPanel.add(new JLabel("Bets: " + vm.getBets(), SwingConstants.CENTER));
        centerPanel.add(new JLabel("Games Played: " + vm.getGamesPlayed(), SwingConstants.CENTER));

        JButton returnBtn = new JButton("Return to Home");
        returnBtn.setFont(new Font("Arial", Font.BOLD, 35));
        returnBtn.addActionListener(e -> {
            mainMenu.setVisible(true);
            dispose();
        });

        add(centerPanel, BorderLayout.CENTER);
        add(returnBtn, BorderLayout.SOUTH);

        setVisible(true);
    }
}

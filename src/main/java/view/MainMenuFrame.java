package view;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame(User user) {
        setTitle("BET366 view.Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920,1080);
        setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel("Welcome to BET366", SwingConstants.CENTER);
        JButton profileBtn = new JButton("Profile");
        JButton betHistoryBtn = new JButton("View Bet History");
        JButton depositBtn = new JButton("Deposit / Withdraw");
        JButton logoutBtn = new JButton("Logout");

        add(title);
        add(profileBtn);
        add(betHistoryBtn);
        add(depositBtn);
        add(logoutBtn);

        profileBtn.addActionListener(e -> {
            new ProfileFrame(user, this);
            setVisible(false);
        });

        depositBtn.addActionListener(e -> {
            new WalletUI(user, this);
            setVisible(false);
        });

        setVisible(true);
    }
}

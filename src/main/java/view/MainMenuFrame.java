package view;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame(User user) {
        setTitle("BET366 Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome to BET366", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 15));
        panel.setOpaque(false);

        JButton profileBtn = createMenuButton("Profile");
        JButton betHistoryBtn = createMenuButton("View Bet History");
        JButton playBetGameBtn = createMenuButton("Play Bet Game");
        JButton depositBtn = createMenuButton("Deposit / Withdraw");
        JButton logoutBtn = createMenuButton("Logout");

        panel.add(profileBtn);
        panel.add(betHistoryBtn);
        panel.add(playBetGameBtn);
        panel.add(depositBtn);
        panel.add(logoutBtn);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        profileBtn.addActionListener(e -> {
            new ProfileFrame(user, this);
            setVisible(false);
        });

        depositBtn.addActionListener(e -> {
            new WalletUI(user, this);
            setVisible(false);
        });

        playBetGameBtn.addActionListener(e -> {
            new SportbetFrame(user, this);
            setVisible(false);
        });

        setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 28));
        btn.setForeground(Color.BLACK);
        btn.setBackground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        btn.setFocusPainted(false);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        return btn;
    }
}

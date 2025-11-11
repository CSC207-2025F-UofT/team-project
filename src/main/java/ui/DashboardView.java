package ui;

import javax.swing.*;
import java.awt.*;

/**
 * The main dashboard screen shown after successful login.
 */
public class DashboardView extends JFrame {

    public DashboardView(Runnable onLogout) {
        setTitle("FinWise Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // only close this window
        setLocationRelativeTo(null);

        // Layout Setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to FinWise!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Buttons for Use Cases
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JButton expensesBtn = new JButton("Track Expenses");
        JButton trendsBtn = new JButton("Financial Trends");
        JButton stockBtn = new JButton("Stock Prices");
        JButton investBtn = new JButton("Simulated Investment");
        JButton portfolioBtn = new JButton("Portfolio Analysis");
        JButton newsBtn = new JButton("Market News");

        buttonPanel.add(expensesBtn);
        buttonPanel.add(trendsBtn);
        buttonPanel.add(stockBtn);
        buttonPanel.add(investBtn);
        buttonPanel.add(portfolioBtn);
        buttonPanel.add(newsBtn);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            onLogout.run();  // switch back to loginView
            dispose();       // close dashboard
        });
        mainPanel.add(logoutBtn, BorderLayout.SOUTH);

        add(mainPanel);
    }
}

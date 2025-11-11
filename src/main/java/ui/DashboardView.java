package ui;

import javax.swing.*;
import java.awt.*;

/**
 * The main dashboard screen shown after successful login.
 * Acts as a navigation hub to different use cases in the FinWise app.
 */
public class DashboardView extends JFrame {

    public DashboardView(
            Runnable onLogout,
            Runnable onTrackExpenses,
            Runnable onFinancialTrends,
            Runnable onStockPrices,
            Runnable onSimulatedInvestment,
            Runnable onPortfolioAnalysis,
            Runnable onMarketNews
    ) {
        setTitle("FinWise Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        mainPanel.add(logoutBtn, BorderLayout.SOUTH);

        // Add action listeners (connect each button to its callback)
        logoutBtn.addActionListener(e -> {
            onLogout.run();
            dispose();
        });
        expensesBtn.addActionListener(e -> onTrackExpenses.run());
        trendsBtn.addActionListener(e -> onFinancialTrends.run());
        stockBtn.addActionListener(e -> onStockPrices.run());
        investBtn.addActionListener(e -> onSimulatedInvestment.run());
        portfolioBtn.addActionListener(e -> onPortfolioAnalysis.run());
        newsBtn.addActionListener(e -> onMarketNews.run());

        add(mainPanel);
    }
}


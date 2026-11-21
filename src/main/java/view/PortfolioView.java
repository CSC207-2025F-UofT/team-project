package view;

import interface_adapter.portfolio.PortfolioController;
import presentation.portfolio.PortfolioViewModel;
import use_case.portfolio.HoldingOutputData;
import use_case.portfolio.TransactionOutputData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Swing view for the simulated investment portfolio.
 */
public final class PortfolioView extends JPanel {

    private final PortfolioController controller;
    private final PortfolioViewModel viewModel;
    private final String username;

    private final JLabel cashLabel = new JLabel();
    private final JLabel valueLabel = new JLabel();
    private final JLabel profitLossLabel = new JLabel();
    private final JLabel messageLabel = new JLabel(" ");

    private final JTextField buyTickerField = new JTextField(8);
    private final JTextField buyQuantityField = new JTextField(5);

    private final JTextField sellTickerField = new JTextField(8);
    private final JTextField sellQuantityField = new JTextField(5);

    private final DefaultTableModel holdingsModel =
            new DefaultTableModel(
                    new String[]{"Ticker", "Quantity", "Avg. Purchase Price",
                            "Current Price", "Market Value"},
                    0
            );

    private final DefaultTableModel transactionsModel =
            new DefaultTableModel(
                    new String[]{"Type", "Ticker", "Quantity", "Price",
                            "Timestamp"},
                    0
            );

    private final JTable holdingsTable = new JTable(holdingsModel);
    private final JTable transactionsTable = new JTable(transactionsModel);

    public PortfolioView(
            PortfolioController controller,
            PortfolioViewModel viewModel,
            String username) {

        this.controller = Objects.requireNonNull(controller);
        this.viewModel = Objects.requireNonNull(viewModel);
        this.username = Objects.requireNonNull(username);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createSummaryPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createMessagePanel(), BorderLayout.SOUTH);

        configureTables();
        refreshPortfolio();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));

        cashLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        profitLossLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(createSummaryCard("Available Cash", cashLabel));
        panel.add(createSummaryCard("Portfolio Value", valueLabel));
        panel.add(createSummaryCard("Profit / Loss", profitLossLabel));

        return panel;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(valueLabel);
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel tradePanel = createTradePanel();

        JPanel tablePanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablePanel.add(createTablePanel("Holdings", holdingsTable));
        tablePanel.add(createTablePanel("Transaction History", transactionsTable));

        panel.add(tradePanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTradePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        panel.add(createBuyPanel());
        panel.add(createSellPanel());

        return panel;
    }

    private JPanel createBuyPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Buy Stocks"));

        JButton buyButton = new JButton("Buy");

        panel.add(new JLabel("Ticker:"));
        panel.add(buyTickerField);
        panel.add(new JLabel("Quantity:"));
        panel.add(buyQuantityField);
        panel.add(buyButton);

        buyButton.addActionListener(e -> handleBuy());

        return panel;
    }

    private JPanel createSellPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Sell Stocks"));

        JButton sellButton = new JButton("Sell");

        panel.add(new JLabel("Ticker:"));
        panel.add(sellTickerField);
        panel.add(new JLabel("Quantity:"));
        panel.add(sellQuantityField);
        panel.add(sellButton);

        sellButton.addActionListener(e -> handleSell());

        return panel;
    }

    private JPanel createTablePanel(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMessagePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> refreshPortfolio());

        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.EAST);

        return panel;
    }

    private void configureTables() {
        holdingsTable.setFillsViewportHeight(true);
        transactionsTable.setFillsViewportHeight(true);

        holdingsTable.setAutoCreateRowSorter(true);
        transactionsTable.setAutoCreateRowSorter(true);

        holdingsTable.setDefaultEditor(Object.class, null);
        transactionsTable.setDefaultEditor(Object.class, null);
    }

    private void handleBuy() {
        handleTrade(
                buyTickerField,
                buyQuantityField,
                true
        );
    }

    private void handleSell() {
        handleTrade(
                sellTickerField,
                sellQuantityField,
                false
        );
    }

    private void handleTrade(
            JTextField tickerField,
            JTextField quantityField,
            boolean isBuy) {

        String ticker = tickerField.getText().trim().toUpperCase();
        String quantityText = quantityField.getText().trim();

        if (ticker.isEmpty() || quantityText.isEmpty()) {
            showMessage("Please enter a ticker and quantity.", false);
            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(quantityText);

            if (quantity <= 0) {
                showMessage("Quantity must be positive.", false);
                return;
            }
        }
        catch (NumberFormatException ex) {
            showMessage("Quantity must be a valid integer.", false);
            return;
        }

        try {
            if (isBuy) {
                controller.buy(username, ticker, quantity);
            }
            else {
                controller.sell(username, ticker, quantity);
            }

            String tradeMessage = viewModel.getMessage();
            boolean tradeSuccess = viewModel.isSuccess();

            controller.viewPortfolio(username);
            controller.viewTransactionHistory(username);
            updateDisplay();

            showMessage(tradeMessage, tradeSuccess);

            if (tradeSuccess) {
                tickerField.setText("");
                quantityField.setText("");
            }
        }
        catch (RuntimeException ex) {
            showMessage("Unable to complete the trade.", false);
        }
    }

    private void refreshPortfolio() {
        try {
            controller.viewPortfolio(username);
            controller.viewTransactionHistory(username);
            updateDisplay();
            showMessage("Portfolio refreshed.", true);
        }
        catch (RuntimeException ex) {
            showMessage("Unable to load portfolio.", false);
        }
    }

    private void updateDisplay() {
        cashLabel.setText(formatMoney(viewModel.getAvailableCash()));
        valueLabel.setText(formatMoney(viewModel.getPortfolioValue()));
        profitLossLabel.setText(formatMoney(viewModel.getProfitLoss()));

        holdingsModel.setRowCount(0);

        for (HoldingOutputData holding : viewModel.getHoldings()) {
            holdingsModel.addRow(new Object[]{
                    holding.getTicker(),
                    holding.getQuantity(),
                    formatMoney(holding.getAveragePurchasePrice()),
                    formatMoney(holding.getCurrentPrice()),
                    formatMoney(holding.getMarketValue())
            });
        }

        transactionsModel.setRowCount(0);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (TransactionOutputData transaction : viewModel.getTransactions()) {
            String timestamp = transaction.getTimestamp() == null
                    ? ""
                    : formatter.format(
                            transaction.getTimestamp()
                                    .atZone(java.time.ZoneId.systemDefault()));

            transactionsModel.addRow(new Object[]{
                    transaction.getType(),
                    transaction.getTicker(),
                    transaction.getQuantity(),
                    formatMoney(transaction.getPrice()),
                    timestamp
            });
        }
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }

        return String.format("$%.2f", amount);
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message == null ? "" : message);
        messageLabel.setForeground(success ? new Color(0, 128, 0) : Color.RED);
    }
}

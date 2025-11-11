package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class WalletUI extends JFrame {
    private final JLabel balanceLabel;
    private final JTextField amountField;
    private final JTextArea historyArea;
    private BigDecimal balance = new BigDecimal("100.00");

    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(Locale.CANADA);

    public WalletUI() {
        setTitle("Wallet");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== 顶部余额显示 =====
        balanceLabel = new JLabel("Balance: " + CURRENCY.format(balance), SwingConstants.CENTER);
        add(balanceLabel, BorderLayout.NORTH);

        // ===== 中间输入区 =====
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        inputPanel.add(depositBtn);
        inputPanel.add(withdrawBtn);

        add(inputPanel, BorderLayout.CENTER);

        // ===== 底部交易历史 =====
        historyArea = new JTextArea(10, 30);
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.SOUTH);

        // ===== 按钮事件 =====
        depositBtn.addActionListener(e -> deposit());
        withdrawBtn.addActionListener(e -> withdraw());
    }

    private void deposit() {
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Please enter a positive amount.");
                return;
            }
            balance = balance.add(amount);
            updateUI("Deposited " + CURRENCY.format(amount));
        } catch (NumberFormatException ex) {
            showError("Invalid input. Please enter a valid number.");
        }
    }

    private void withdraw() {
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Please enter a positive amount.");
                return;
            }
            if (balance.compareTo(amount) < 0) {
                showError("Insufficient funds!");
                return;
            }
            balance = balance.subtract(amount);
            updateUI("Withdrew " + CURRENCY.format(amount));
        } catch (NumberFormatException ex) {
            showError("Invalid input. Please enter a valid number.");
        }
    }

    private void updateUI(String action) {
        balanceLabel.setText("Balance: " + CURRENCY.format(balance));
        historyArea.append(action + "\n");
        amountField.setText("");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WalletUI().setVisible(true));
    }
}

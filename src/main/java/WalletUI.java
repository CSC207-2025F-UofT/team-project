import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
public class WalletUI {

    private final JFrame frame;
    private final JLabel balanceLabel;
    private final JTextField amountField;
    private final JTextArea historyArea;
    private BigDecimal balance = new BigDecimal("100.00");

    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(Locale.CANADA);

    public WalletUI() {
        frame = new JFrame("Wallet");
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        balanceLabel = new JLabel("Balance: " + CURRENCY.format(balance), SwingConstants.CENTER);
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 18f));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        amountField = new JTextField(10);
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");

        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(depositBtn);
        inputPanel.add(withdrawBtn);

        historyArea = new JTextArea(10, 28);
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);

        frame.setLayout(new BorderLayout(8, 8));
        frame.add(balanceLabel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        depositBtn.addActionListener(this::handleDeposit);
        withdrawBtn.addActionListener(this::handleWithdraw);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void handleDeposit(ActionEvent e) {
        try {
            BigDecimal amt = parseAmount(amountField.getText());
            balance = balance.add(amt);
            updateUI("Deposit", amt);
        } catch (IllegalArgumentException ex) {
            showError("Invalid Input");
        }
    }

    private void handleWithdraw(ActionEvent e) {
        try {
            BigDecimal amt = parseAmount(amountField.getText());
            if (balance.compareTo(amt) < 0) {
                showError("Insufficient Funds");
                return;
            }
            balance = balance.subtract(amt);
            updateUI("Withdraw", amt);
        } catch (IllegalArgumentException ex) {
            showError("Invalid Input");
        }
    }

    private void updateUI(String type, BigDecimal amt) {
        balanceLabel.setText("Balance: " + CURRENCY.format(balance));
        historyArea.append(type + ": " + CURRENCY.format(amt) + "\n");
        amountField.setText("");
    }

    private BigDecimal parseAmount(String text) {
        String t = text.trim();
        if (t.isEmpty()) throw new IllegalArgumentException();
        BigDecimal val = new BigDecimal(t);
        if (val.signum() <= 0) throw new IllegalArgumentException();
        return val;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WalletUI().show());
    }
}

package presentation;

import entity.Transaction;
import entity.TransactionType;
import interface_adapter.transactions.TransactionDashboardController;
import interface_adapter.transactions.TransactionDashboardViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Swing-only presentation. It renders View Model state and forwards user actions to the Controller. */
public final class FinanceTrackerView extends JFrame {
    private final TransactionDashboardController controller;
    private final TransactionDashboardViewModel viewModel;
    private final JComboBox<String> users = new JComboBox<>();
    private final DefaultListModel<Transaction> incomeRows = new DefaultListModel<>();
    private final DefaultListModel<Transaction> expenseRows = new DefaultListModel<>();
    private final JList<Transaction> incomeList = new JList<>(incomeRows);
    private final JList<Transaction> expenseList = new JList<>(expenseRows);
    private final JLabel incomeTotal = new JLabel();
    private final JLabel expenseTotal = new JLabel();
    private final JLabel balance = new JLabel();
    private boolean updatingUsers;

    public FinanceTrackerView(TransactionDashboardController controller,
                              TransactionDashboardViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        buildWindow();
        controller.loadUsers();
        refresh();
        if (users.getItemCount() > 0) {
            users.setSelectedIndex(0);
            loadSelectedUser();
        }
        setVisible(true);
    }

    private void buildWindow() {
        setTitle("Finance Tracker");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBorder(new EmptyBorder(24, 28, 24, 28));
        root.setBackground(new Color(245, 247, 250));

        JLabel heading = new JLabel("Finance Tracker");
        heading.setFont(new Font("SansSerif", Font.BOLD, 28));
        JLabel subtitle = new JLabel("Your financial overview");
        subtitle.setForeground(new Color(100, 110, 125));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(heading);
        titles.add(subtitle);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        users.addActionListener(event -> {
            if (!updatingUsers) loadSelectedUser();
        });
        actions.add(new JLabel("Profile:"));
        actions.add(users);
        JButton add = new JButton("Add Transaction");
        add.addActionListener(event -> showAddDialog());
        actions.add(add);
        top.add(titles, BorderLayout.WEST);
        top.add(actions, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        incomeList.setCellRenderer(transactionRenderer());
        expenseList.setCellRenderer(transactionRenderer());
        JPanel lists = new JPanel(new GridLayout(1, 2, 18, 0));
        lists.setOpaque(false);
        lists.add(transactionPanel("Income", incomeList, TransactionType.INCOME));
        lists.add(transactionPanel("Expenses", expenseList, TransactionType.EXPENSE));
        root.add(lists, BorderLayout.CENTER);

        JPanel totals = new JPanel(new GridLayout(1, 3, 18, 0));
        totals.setBackground(Color.WHITE);
        totals.setBorder(new LineBorder(new Color(220, 226, 234), 1, true));
        styleTotal(incomeTotal, new Color(30, 140, 85));
        styleTotal(expenseTotal, new Color(190, 65, 65));
        styleTotal(balance, new Color(35, 48, 65));
        totals.add(incomeTotal);
        totals.add(expenseTotal);
        totals.add(balance);
        root.add(totals, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void styleTotal(JLabel label, Color color) {
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        label.setForeground(color);
        label.setBorder(new EmptyBorder(18, 18, 18, 18));
    }

    private JPanel transactionPanel(String title, JList<Transaction> list, TransactionType type) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 19));
        panel.add(label, BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(0, 10));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(12, 12, 12, 12));
        body.add(new JScrollPane(list), BorderLayout.CENTER);
        JButton delete = new JButton("Delete Selected");
        delete.addActionListener(event -> deleteSelected(type, list));
        body.add(delete, BorderLayout.SOUTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private ListCellRenderer<? super Transaction> transactionRenderer() {
        return (list, transaction, index, selected, focus) -> {
            JLabel label = new JLabel(transaction.getDate() + " | "
                + transaction.getDescription() + " | $"
                + transaction.getAmount().toPlainString());
            label.setOpaque(true);
            label.setBorder(new EmptyBorder(7, 7, 7, 7));
            label.setBackground(selected ? new Color(215, 230, 248) : Color.WHITE);
            return label;
        };
    }

    private void loadSelectedUser() {
        Object selected = users.getSelectedItem();
        if (selected == null) return;
        controller.loadUser(selected.toString());
        refresh();
    }

    private void showAddDialog() {
        JComboBox<String> type = new JComboBox<>(new String[]{"Income", "Expense"});
        JTextField amount = new JTextField();
        JTextField description = new JTextField();
        JTextField date = new JTextField(LocalDate.now().toString());
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Type:")); form.add(type);
        form.add(new JLabel("Amount:")); form.add(amount);
        form.add(new JLabel("Description:")); form.add(description);
        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(date);
        int choice = JOptionPane.showConfirmDialog(this, form, "Add Transaction",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return;

        try {
            TransactionType transactionType = type.getSelectedIndex() == 0
                ? TransactionType.INCOME : TransactionType.EXPENSE;
            Transaction transaction = new Transaction(0, transactionType,
                new BigDecimal(amount.getText().trim()), description.getText().trim(),
                LocalDate.parse(date.getText().trim()));
            controller.addTransaction(selectedUser(), transaction);
            refresh();
            showMessage();
        } catch (NumberFormatException exception) {
            showError("Enter a valid amount.");
        } catch (RuntimeException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteSelected(TransactionType type, JList<Transaction> list) {
        Transaction selected = list.getSelectedValue();
        if (selected == null) {
            showError("Select a transaction first.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
            "Delete " + selected.getDescription() + "?", "Confirm deletion",
            JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;
        controller.deleteTransaction(selectedUser(), type, selected.getId());
        refresh();
        showMessage();
    }

    private String selectedUser() {
        Object selected = users.getSelectedItem();
        if (selected == null) throw new IllegalStateException("Select a user profile.");
        return selected.toString();
    }

    private void refresh() {
        String selected = (String) users.getSelectedItem();
        updatingUsers = true;
        users.removeAllItems();
        for (String user : viewModel.getUsers()) users.addItem(user);
        if (selected != null) users.setSelectedItem(selected);
        updatingUsers = false;

        incomeRows.clear();
        for (Transaction transaction : viewModel.getIncome()) incomeRows.addElement(transaction);
        expenseRows.clear();
        for (Transaction transaction : viewModel.getExpenses()) expenseRows.addElement(transaction);

        incomeTotal.setText("Income: $" + viewModel.getTotalIncome().toPlainString());
        expenseTotal.setText("Expenses: $" + viewModel.getTotalExpenses().toPlainString());
        balance.setText("Balance: $" + viewModel.getBalance().toPlainString());
    }

    private void showMessage() {
        if (!viewModel.getMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this, viewModel.getMessage(),
                "Finance Tracker", viewModel.isError()
                    ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Finance Tracker",
            JOptionPane.ERROR_MESSAGE);
    }
}

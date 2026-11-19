
import interface_adapter.TransactionControl;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import java.time.LocalDate;

public class FinanceTracker extends JFrame {

  // Colours used throughout the window
  private final Color backgroundColor = new Color(245, 247, 250);
  private final Color cardColor = Color.WHITE;
  private final Color titleColor = new Color(35, 48, 65);
  private final Color borderColor = new Color(220, 226, 234);

  // API client
  private final ApiClient api = new ApiClient();

  // Dropdown
  private JComboBox<String> userDropdown;

  // Labels that will update when switching users
  private JLabel incomeTotalLabel;
  private JLabel expenseTotalLabel;
  private JLabel balanceLabel;

  // Transaction lists
  private DefaultListModel<ApiTransaction> incomeModel;
  private DefaultListModel<ApiTransaction> expenseModel;
  private JList<ApiTransaction> incomeList;
  private JList<ApiTransaction> expenseList;


  public FinanceTracker() {

    // Start API server
    try {
      ApiServer.startServer();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    setTitle("Finance Tracker");
    setSize(1200, 750);

    //Stop the server when application closes
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent e) {
        ApiServer.stopServer();
        dispose();
        System.exit(0);
      }
    });

    setLocationRelativeTo(null);

    // app.Main panel
    JPanel mainPanel = new JPanel(new BorderLayout(0, 25));
    mainPanel.setBackground(backgroundColor);
    mainPanel.setBorder(new EmptyBorder(30, 40, 35, 40));

    // Heading
    JPanel headingPanel = new JPanel();
    headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
    headingPanel.setBackground(backgroundColor);

    JLabel headingLabel = new JLabel("Finance Tracker");
    headingLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
    headingLabel.setForeground(titleColor);

    JLabel subtitleLabel = new JLabel("Your financial overview");
    subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
    subtitleLabel.setForeground(new Color(110, 120, 135));

    headingPanel.add(headingLabel);
    headingPanel.add(Box.createVerticalStrut(5));
    headingPanel.add(subtitleLabel);

    // User selection
    JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    userPanel.setBackground(backgroundColor);

    JLabel userLabel = new JLabel("Viewing account: ");
    userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

    //Grabs the Usernames
    try {
      TransactionControl control =
          new TransactionControl("UserTransactions.json");

      List<String> users = control.getUsers();

      userDropdown = new JComboBox<>(
          users.toArray(new String[0])
      );

    } catch (IOException e) {

      JOptionPane.showMessageDialog(
          this,
          "Could not load users: " + e.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE
      );

      return;
    }
    userDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));

    userPanel.add(userLabel);
    userPanel.add(userDropdown);

    // Add transaction button
    JButton addTransactionButton = new JButton("Add Transaction");
    addTransactionButton.setFont(new Font("SansSerif", Font.BOLD, 13));

    addTransactionButton.addActionListener(e -> showAddTransactionDialog());

    userPanel.add(addTransactionButton);

    headingPanel.add(Box.createVerticalStrut(15));
    headingPanel.add(userPanel);

    mainPanel.add(headingPanel, BorderLayout.NORTH);

    // Sections
    JPanel sectionsPanel = new JPanel(new BorderLayout(0, 25));
    sectionsPanel.setBackground(backgroundColor);

    JPanel topPanel = new JPanel(new GridLayout(1, 2, 25, 0));
    topPanel.setBackground(backgroundColor);

    // Income section
    incomeModel = new DefaultListModel<>();
    JPanel incomeSection = createTransactionSection(
        "Income", incomeModel, "income"
    );

    // Expenses section
    expenseModel = new DefaultListModel<>();
    JPanel expenseSection = createTransactionSection(
        "Expenses", expenseModel, "expenses"
    );

    topPanel.add(incomeSection);
    topPanel.add(expenseSection);

    // Analytics
    JPanel analyticsSection = createAnalyticsSection();

    sectionsPanel.add(topPanel, BorderLayout.CENTER);
    sectionsPanel.add(analyticsSection, BorderLayout.SOUTH);

    mainPanel.add(sectionsPanel, BorderLayout.CENTER);

    add(mainPanel);

    // Listen for dropdown changes
    userDropdown.addActionListener(e -> {
      String selectedUser =
          (String) userDropdown.getSelectedItem();

      loadUserData(selectedUser);
    });

    // Load initial user
    loadUserData("Meshac");

    setVisible(true);
  }

  // Creates a transaction section

  private JPanel createTransactionSection(
      String title, DefaultListModel<ApiTransaction> model,
      String transactionType) {

    JPanel sectionPanel = new JPanel(new BorderLayout(0, 10));
    sectionPanel.setBackground(backgroundColor);

    JLabel sectionTitle = new JLabel(title);
    sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 19));
    sectionTitle.setForeground(titleColor);

    // Create the transaction list
    JList<ApiTransaction> transactionList = new JList<>(model);

    transactionList.setFont(new Font("SansSerif", Font.PLAIN, 14));

    // Tell Swing how to display each transaction
    transactionList.setCellRenderer((list, transaction, index,
                                     isSelected, cellHasFocus) -> {

      JLabel label = new JLabel(
          transaction.getDate() + " | "
              + transaction.getDescription() + " | $"
              + String.format("%.2f", transaction.getAmount())
      );

      label.setOpaque(true);
      label.setBorder(new EmptyBorder(5, 5, 5, 5));

      if (isSelected) {
        label.setBackground(new Color(210, 225, 245));
      } else {
        label.setBackground(Color.WHITE);
      }

      return label;
    });

    // Keep references to the lists
    if (transactionType.equals("income")) {
      incomeList = transactionList;
    } else {
      expenseList = transactionList;
    }

    JScrollPane scrollPane = new JScrollPane(transactionList);

    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(cardColor);
    contentPanel.setBorder(new CompoundBorder(
        new LineBorder(borderColor, 1, true),
        new EmptyBorder(15, 15, 15, 15)
    ));

    contentPanel.add(scrollPane, BorderLayout.CENTER);

    // Delete button
    JButton deleteButton = new JButton("Delete Selected");

    deleteButton.addActionListener(e ->
        deleteSelectedTransaction(transactionType)
    );

    contentPanel.add(deleteButton, BorderLayout.SOUTH);

    sectionPanel.add(sectionTitle, BorderLayout.NORTH);
    sectionPanel.add(contentPanel, BorderLayout.CENTER);

    return sectionPanel;
  }


  // Creates analytics section
  private JPanel createAnalyticsSection() {

    JPanel sectionPanel = new JPanel(new BorderLayout(0, 10));
    sectionPanel.setBackground(backgroundColor);

    JLabel sectionTitle = new JLabel("Analytics");
    sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 19));
    sectionTitle.setForeground(titleColor);

    JPanel contentPanel = new JPanel(new GridLayout(1, 3, 20, 0));
    contentPanel.setBackground(cardColor);
    contentPanel.setBorder(new CompoundBorder(
        new LineBorder(borderColor, 1, true),
        new EmptyBorder(20, 20, 20, 20)
    ));

    incomeTotalLabel = new JLabel("Income: $0.00");
    expenseTotalLabel = new JLabel("Expenses: $0.00");
    balanceLabel = new JLabel("Balance: $0.00");

    incomeTotalLabel.setForeground(new Color(30, 150, 90));
    expenseTotalLabel.setForeground(new Color(200, 65, 65));
    balanceLabel.setForeground(titleColor);

    incomeTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    expenseTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

    contentPanel.add(incomeTotalLabel);
    contentPanel.add(expenseTotalLabel);
    contentPanel.add(balanceLabel);

    sectionPanel.add(sectionTitle, BorderLayout.NORTH);
    sectionPanel.add(contentPanel, BorderLayout.CENTER);

    return sectionPanel;
  }


  // Opens a form to add a new transaction
  private void showAddTransactionDialog() {

    // Create form fields
    JComboBox<String> typeDropdown = new JComboBox<>(
        new String[]{"Income", "Expense"}
    );

    JTextField amountField = new JTextField();
    JTextField descriptionField = new JTextField();
    JTextField dateField = new JTextField(LocalDate.now().toString());

    // Create form panel
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

    formPanel.add(new JLabel("Transaction Type:"));
    formPanel.add(typeDropdown);

    formPanel.add(new JLabel("Amount:"));
    formPanel.add(amountField);

    formPanel.add(new JLabel("Description:"));
    formPanel.add(descriptionField);

    formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
    formPanel.add(dateField);

    // Display form
    int result = JOptionPane.showConfirmDialog(
        this,
        formPanel,
        "Add Transaction",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );

    // Only continue if the user clicks OK
    if (result != JOptionPane.OK_OPTION) {
      return;
    }

    try {

      // Retrieve form values
      String username =
          (String) userDropdown.getSelectedItem();

      String type = typeDropdown.getSelectedItem().toString();

      // Convert Expense to expenses, Income to income
      if (type.equals("Expense")) {
        type = "expenses";
      } else {
        type = "income";
      }

      double amount = Double.parseDouble(amountField.getText());

      String description = descriptionField.getText().trim();

      String date = dateField.getText().trim();

      // Validate input
      if (amount <= 0 || description.isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "Please enter a positive amount and description.",
            "Invalid Input",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }

      // Validate date format
      LocalDate.parse(date);

      // Send transaction to backend
      api.addTransaction(
          username,
          type,
          amount,
          description,
          date
      );

      // Refresh dashboard
      loadUserData(username);

      JOptionPane.showMessageDialog(
          this,
          "Transaction added successfully!",
          "Success",
          JOptionPane.INFORMATION_MESSAGE
      );

    } catch (NumberFormatException e) {

      JOptionPane.showMessageDialog(
          this,
          "Please enter a valid numerical amount.",
          "Invalid Amount",
          JOptionPane.ERROR_MESSAGE
      );

    } catch (java.time.format.DateTimeParseException e) {

      JOptionPane.showMessageDialog(
          this,
          "Please enter a valid date in YYYY-MM-DD format.",
          "Invalid Date",
          JOptionPane.ERROR_MESSAGE
      );

    } catch (IOException | InterruptedException e) {

      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }

      JOptionPane.showMessageDialog(
          this,
          "Could not save transaction.\n" + e.getMessage(),
          "API Error",
          JOptionPane.ERROR_MESSAGE
      );
    }
  }


  private void deleteSelectedTransaction(String type) {

    String username = (String) userDropdown.getSelectedItem();

    // Get selected transaction directly from the JList
    ApiTransaction selectedTransaction;

    if (type.equals("income")) {
      selectedTransaction = incomeList.getSelectedValue();
    } else {
      selectedTransaction = expenseList.getSelectedValue();
    }

    if (selectedTransaction == null) {
      JOptionPane.showMessageDialog(
          this,
          "Please select a transaction first."
      );
      return;
    }

    int confirmation = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete "
            + selectedTransaction.getDescription() + "?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmation != JOptionPane.YES_OPTION) {
      return;
    }

    try {

      api.deleteTransaction(
          username,
          type,
          selectedTransaction.getId()
      );

      loadUserData(username);

      JOptionPane.showMessageDialog(
          this,
          "Transaction deleted successfully!"
      );

    } catch (IOException | InterruptedException e) {

      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }

      JOptionPane.showMessageDialog(
          this,
          "Could not delete transaction.\n" + e.getMessage(),
          "API Error",
          JOptionPane.ERROR_MESSAGE
      );
    }
  }



  // Retrieves and displays the selected user's data
  private void loadUserData(String username) {

    // Clear previous transactions
    incomeModel.clear();
    expenseModel.clear();

    try {

      // Retrieve data through the API
      List<ApiTransaction> income =
          api.getIncome(username);

      List<ApiTransaction> expenses =
          api.getExpenses(username);

      double totalIncome = 0;
      double totalExpenses = 0;

      // Display income
      for (ApiTransaction transaction : income) {

        incomeModel.addElement(transaction);

        totalIncome += transaction.getAmount();
      }

      // Display expenses
      for (ApiTransaction transaction : expenses) {

        expenseModel.addElement(transaction);

        totalExpenses += transaction.getAmount();
      }

      // Calculate balance
      double balance = totalIncome - totalExpenses;

      // Update analytics
      incomeTotalLabel.setText(
          String.format("Income: $%.2f", totalIncome)
      );

      expenseTotalLabel.setText(
          String.format("Expenses: $%.2f", totalExpenses)
      );

      balanceLabel.setText(
          String.format("Balance: $%.2f", balance)
      );

    } catch (IOException | InterruptedException e) {

      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }

      JOptionPane.showMessageDialog(
          this,
          "Could not retrieve user transactions.\n"
              + e.getMessage(),
          "API Error",
          JOptionPane.ERROR_MESSAGE
      );
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new FinanceTracker());
  }
}


package test.usecase.transaction;

import entity.Transaction;
import use_case.transaction_categorizer.TransactionCategorizerService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Simple categorizer for testing without API
class SimpleTransactionCategorizerService extends TransactionCategorizerService {
    public SimpleTransactionCategorizerService() {
        super(null); // no GeminiClient needed
    }

    @Override
    public void categorizeTransactions(List<Transaction> transactions) {
        for (Transaction t : transactions) {
            String desc = t.getDescription().toLowerCase();
            String category;
            if (desc.contains("starbucks")) category = "dining out";
            else if (desc.contains("whole foods")) category = "groceries";
            else if (desc.contains("netflix")) category = "entertainment";
            else if (desc.contains("gym")) category = "fitness";
            else if (desc.contains("uber")) category = "transportation";
            else category = "miscellaneous";

            t.setCategory(category);
        }
    }
}

public class TransactionCategorizerTester {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TransactionCategorizerTester::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Transaction Categorizer Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JButton categorizeButton = new JButton("Categorize Transactions");

        // Dummy transactions
        List<Transaction> dummyTransactions = new ArrayList<>();
        dummyTransactions.add(new Transaction(LocalDate.now(), "Starbucks coffee", 5.0, ""));
        dummyTransactions.add(new Transaction(LocalDate.now(), "Whole Foods groceries", 50.0, ""));
        dummyTransactions.add(new Transaction(LocalDate.now(), "Netflix subscription", 15.0, ""));
        dummyTransactions.add(new Transaction(LocalDate.now(), "Gym membership", 30.0, ""));
        dummyTransactions.add(new Transaction(LocalDate.now(), "Uber ride", 12.5, ""));

        categorizeButton.addActionListener(e -> {
            // Use simple categorizer for testing
            SimpleTransactionCategorizerService categorizer = new SimpleTransactionCategorizerService();
            categorizer.categorizeTransactions(dummyTransactions);

            // Display results
            StringBuilder sb = new StringBuilder("Categorized Transactions:\n\n");
            for (Transaction t : dummyTransactions) {
                sb.append(t.getDateString())
                        .append(" | ").append(t.getDescription())
                        .append(" | $").append(t.getAmount())
                        .append(" | Category: ").append(t.getCategory())
                        .append("\n");
            }
            outputArea.setText(sb.toString());
        });

        frame.setLayout(new BorderLayout());
        frame.add(categorizeButton, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


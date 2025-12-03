package view;

import entity.Transaction;
import interface_adapter.CategorizerViewModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Transaction Categorizer View (Swing) for AppBuilder integration
 */
public class TransactionCategorizerView extends JPanel {

    private final String viewName = "transaction categorizer";
    private final CategorizerViewModel viewModel;
    private final JButton categorizeButton = new JButton("Categorize Transactions");
    private final JTextArea resultArea = new JTextArea(20, 50);

    public TransactionCategorizerView(CategorizerViewModel viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());

        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(categorizeButton, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        List<Transaction> transactions = loadDummyTransactions();

        categorizeButton.addActionListener(e -> {
            // Dummy categorization
            for (Transaction t : transactions) {
                t.setCategory(dummyCategorize(t));
            }
            viewModel.setCategorizedTransactions(transactions);
            updateResultArea();
        });
    }

    public String getViewName() {
        return viewName;
    }

    private void updateResultArea() {
        StringBuilder sb = new StringBuilder("Categorized Transactions:\n\n");
        for (Transaction t : viewModel.getCategorizedTransactions()) {
            sb.append(t.getDateString())
                    .append(" - ")
                    .append(t.getDescription())
                    .append(" - $")
                    .append(t.getAmount())
                    .append(" - Category: ")
                    .append(t.getCategory())
                    .append("\n");
        }
        resultArea.setText(sb.toString());
    }

    private List<Transaction> loadDummyTransactions() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.now(), "Starbucks Coffee", 5.50, ""));
        list.add(new Transaction(LocalDate.now(), "Grocery Store", 32.75, ""));
        list.add(new Transaction(LocalDate.now(), "Cinema Ticket", 12.00, ""));
        return list;
    }

    private String dummyCategorize(Transaction t) {
        String desc = t.getDescription().toLowerCase();
        if (desc.contains("coffee") || desc.contains("cafe")) return "dining out";
        if (desc.contains("grocery") || desc.contains("supermarket")) return "groceries";
        if (desc.contains("cinema") || desc.contains("movie")) return "entertainment";
        return "miscellaneous";
    }
}


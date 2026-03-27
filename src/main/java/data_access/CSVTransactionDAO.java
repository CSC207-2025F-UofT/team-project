package data_access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entity.Transaction;
import interface_adapter.TransactionDataAccess;

public class CSVTransactionDAO implements TransactionDataAccess {

    private final List<Transaction> transactions;
    private final String csvFilePath;

    public CSVTransactionDAO(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.transactions = loadTransactionsFromCSV();
    }

    private List<Transaction> loadTransactionsFromCSV() {
        List<Transaction> loadedTransactions = new ArrayList<>();

        try {
            File file = new File(csvFilePath);
            if (!file.exists()) {
                System.err.println("CSV file not found: " + csvFilePath);
                System.err.println("Current directory: " + new File(".").getAbsolutePath());
                return loadedTransactions;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] values = line.split(",");
                    if (values.length == 4) {
                        LocalDate date = LocalDate.parse(values[0].trim(), formatter);
                        String category = values[1].trim();
                        double amount = Double.parseDouble(values[2].trim());
                        String description = values[3].trim();
                        String username = values[4].trim();
                        loadedTransactions.add(new Transaction(date, category, amount, description, username));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error parsing CSV data: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Loaded " + loadedTransactions.size() + " transactions from CSV");
        return loadedTransactions;
    }

    @Override
    public List<Transaction> getTransactions(String username) {
        return transactions.stream().filter(t -> t.getUsername().equals(username)).toList();
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactions.add(transaction);
        try (FileWriter fw = new FileWriter(csvFilePath, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            out.printf("%s,%s,%.2f,%s,%s%n",
                    transaction.getDate().format(formatter),
                    transaction.getCategory(),
                    transaction.getAmount(),
                    transaction.getMerchant(),
                    transaction.getUsername()
            );

        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

}

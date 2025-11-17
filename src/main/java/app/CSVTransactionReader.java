import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entity.Transaction;

public class CSVTransactionReader {
    
    public static List<Transaction> readTransactions(String filename) {
        List<Transaction> transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] values = line.split(",");
                if (values.length == 4) {
                    LocalDate date = LocalDate.parse(values[0], formatter);
                    String category = values[1];
                    double amount = Double.parseDouble(values[2]);
                    String description = values[3];
                    
                    transactions.add(new Transaction(date, category, amount, description));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        
        return transactions;
    }
}
package data_access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entity.Transaction;
import interface_adapter.TransactionDataAccess;

public class CSVTransactionDAO implements TransactionDataAccess {
    private List<Transaction> transactions;
    private String csvFilePath;
    
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
                return loadedTransactions; // Return empty list instead of crashing
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
                        
                        loadedTransactions.add(new Transaction(date, category, amount, description));
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
    public List<Transaction> getTransactions(int userId, String month) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        String targetMonthYear = convertToYearMonth(month);
        
        for (Transaction transaction : transactions) {
            String transactionMonthYear = convertToYearMonth(transaction.getMonthYear());
            if (targetMonthYear.equals(transactionMonthYear)) {
                filteredTransactions.add(transaction);
            }
        }
        
        return filteredTransactions;
    }
    
    private String convertToYearMonth(String monthString) {
        String[] parts = monthString.split(" ");
        String monthName = parts[0];
        String year = parts[1];
        
        java.util.Map<String, String> monthMap = new java.util.HashMap<>();
        monthMap.put("Jan", "01"); monthMap.put("Feb", "02"); 
        monthMap.put("Mar", "03"); monthMap.put("Apr", "04");
        monthMap.put("May", "05"); monthMap.put("Jun", "06");
        monthMap.put("Jul", "07"); monthMap.put("Aug", "08");
        monthMap.put("Sep", "09"); monthMap.put("Oct", "10");
        monthMap.put("Nov", "11"); monthMap.put("Dec", "12");
        
        return year + "-" + monthMap.get(monthName);
    }
}
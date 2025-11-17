package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private LocalDate date;
    private String category;
    private double amount;
    private String description;
    
    public Transaction(LocalDate date, String category, double amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = "";
    }
    
    // Getters
    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
    public double getAmount() { return (double) amount; }
    public String getDescription() { return description; }
    
    public String getMonthYear() {
        return date.getMonth().toString().substring(0, 3) + " " + date.getYear();
    }
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | $%.2f | %s", 
            getFormattedDate(), category, amount, description);
    }
}
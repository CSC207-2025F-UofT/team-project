package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private int userId;
    private LocalDate date;
    private String merchant;
    private float amount;
    private String category;
    
    // Constructor for CSV data
    public Transaction(LocalDate date, String category, double amount, String description) {
        this.id = -1;
        this.userId = 1;
        this.date = date;
        this.merchant = description;
        this.amount = (float) amount; 
        this.category = category;
    }
    
    public Transaction(int id, int userId, String dateString, String merchant, float amount, String category) {
        this.id = id;
        this.userId = userId;
        this.date = LocalDate.parse(dateString);
        this.merchant = merchant;
        this.amount = amount;
        this.category = category;
    }
    
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public String getMerchant() { return merchant; }
    public Float getAmount() { return amount; }
    public String getCategory() { return category; }
    
    public String getMonthYear() {
        return date.getMonth().toString().substring(0, 3) + " " + date.getYear();
    }
    
    public String getDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | $%.2f | %s", 
            getDateString(), category, amount, merchant);
    }
}
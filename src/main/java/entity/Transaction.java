package entity;

import java.time.LocalDate;

public class Transaction {
    private String username;
    private final LocalDate date;
    private final String merchant;
    private final float amount;
    private String category;

    // Constructor for CSV data
    public Transaction(LocalDate date, String category, double amount, String description) {
        this.date = date;
        this.merchant = description;
        this.amount = (float) amount;
        this.category = category;
    }

    // Constructor for CSV data
    public Transaction(LocalDate date, String category, double amount, String description, String username) {
        this.date = date;
        this.merchant = description;
        this.amount = (float) amount; 
        this.category = category;
        this.username = username;
    }

    public LocalDate getDate() {return date;}
    public String getMerchant() {return merchant;}
    public String getUsername() {return username;}
    public Float getAmount() { return amount; }
    public String getCategory() { return category; }

    public void setCategory(String category) {
        this.category = category;
    }
}

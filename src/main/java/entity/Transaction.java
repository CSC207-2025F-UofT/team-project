package entity;

import java.time.LocalDate;

public class Transaction {
    private LocalDate date;
    private String description;
    private double amount;
    private String category;

    public Transaction(LocalDate date, String description, double amount, String category) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDateString() { return date.toString(); }
}

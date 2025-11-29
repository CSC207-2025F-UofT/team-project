package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents one spending transaction in the system.
 */
public class Transaction {

    private final LocalDate date;
    private final String category;
    private final double amount;
    private final String description;

    public Transaction(LocalDate date, String category, double amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Required by ChartDataConverter.
     * Returns a month-year key such as "2024-01" or "Jan 2024".
     */
    public String getMonthYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return date.format(formatter);
    }
}

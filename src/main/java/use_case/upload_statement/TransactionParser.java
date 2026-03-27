package use_case.upload_statement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import entity.Transaction;

/**
 * Utility to parse CSV transaction lines into Transaction entities.
 */
public final class TransactionParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TransactionParser() { }

    /**
     * Parse a CSV line expected in the format: date,description,amount,category?
     * Returns a Transaction for the given username.
     * Throws IllegalArgumentException if the line is malformed.
     */
    public static Transaction parseCsvLine(String line, String username) {
        if (line == null) {
            throw new IllegalArgumentException("CSV line is null");
        }

        String[] values = line.split(",", 4);
        if (values.length < 3) {
            throw new IllegalArgumentException("CSV line has too few columns: " + line);
        }

        try {
            LocalDate date = LocalDate.parse(values[0].trim(), FORMATTER);
            String description = values[1].trim();
            double amount = Double.parseDouble(values[2].trim());
            String category = "";
            return new Transaction(date, category, amount, description, username);
        } catch (NumberFormatException | DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid CSV data: " + line, ex);
        }
    }
}

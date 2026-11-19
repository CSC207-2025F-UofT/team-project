package entity;

import java.io.IOException;
import java.time.LocalDate;

public class Income extends Transaction {

  public Income(double amount, LocalDate date, String description, Account account, String transactionCurrency, Category category, String note) throws IOException {
    super(amount, date, description, account, transactionCurrency, category, note, "Revenues.json");
  }

  // Getter
    public Category getCategory() {
        return category;
    }

    // Setter
    public void setCategory(Category category) {
        this.category = category;
    }

    // Add income to account balance
    @Override
    public void execute() throws IOException {
        account.deposit(amount, transactionCurrency);
    }
}

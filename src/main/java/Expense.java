
import java.io.IOException;
import java.time.LocalDate;

public class Expense extends Transaction {

  public Expense(double amount, LocalDate date, String description, Account account, String transactionCurrency, Category category, String note) throws IOException {
    super(amount, date, description, account, transactionCurrency, category, note, "Expenditures.json");
  }

  // Getters
    public Category getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    // Setters
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Deduct expense from account balance
    @Override
    public void execute() throws IOException {
        account.withdraw(amount, transactionCurrency);
    }
}

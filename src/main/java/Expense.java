
import java.io.IOException;
import java.time.LocalDate;

public class Expense extends Transaction {

    private Category category;
    private String note;

    public Expense(int transactionID, double amount,
                   LocalDate date, String description,
                   Account account, Category category,
                   String note, String transactionCurrency) throws IOException {

        super(transactionID, amount, date, description, account, transactionCurrency);

        this.category = category;
        this.note = note;
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

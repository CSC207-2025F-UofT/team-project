
import java.io.IOException;
import java.time.LocalDate;

public class Income extends Transaction {

    private Category category;

    public Income(int transactionID, double amount,
                  LocalDate date, String description,
                  Account account, Category category, String transactionCurrency) throws IOException {

        super(transactionID, amount, date, description, account, transactionCurrency);

        this.category = category;
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

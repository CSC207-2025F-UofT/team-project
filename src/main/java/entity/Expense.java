package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Expense extends AccountTransaction {
    public Expense(BigDecimal amount, LocalDate date, String description, Account account,
                   String transactionCurrency, Category category, String note) {
        super(amount, date, description, account, transactionCurrency, category, note);
    }
    @Override
    public void applyToAccount(BigDecimal amountInAccountCurrency) {
        account.withdraw(amountInAccountCurrency);
    }
}

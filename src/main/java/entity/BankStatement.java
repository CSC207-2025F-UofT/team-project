package entity;
import java.time.YearMonth;
import java.util.List;
import java.util.ArrayList;

public class BankStatement {
    private YearMonth date;
    private List<Transaction> transactions;
    private boolean isCategorized;

    public BankStatement(YearMonth date) {
        this.date = date;
        this.transactions = new ArrayList<Transaction>();
        this.isCategorized = false;
    }

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

    public void addTransactions(List<Transaction> txs) {
        transactions.addAll(txs);
    }

    public void markCategorized() {
        this.isCategorized = true;
    }

    public boolean isCategorized() {
        return this.isCategorized;
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction tx : transactions) {
            balance += tx.getAmount();
        }
        return balance;
    }

    public double getExpenses() {
        double expenses = 0;
        for (Transaction tx : transactions) {
            double transaction = tx.getAmount();
            if (transaction < 0) expenses += transaction;
        }
        return -1 * expenses;
    }

    public double getIncome() {
        double income = 0;
        for (Transaction tx: transactions) {
            double transaction = tx.getAmount();
            if (transaction > 0) income += transaction;
        }
        return income;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public YearMonth getDate() {
        return date;
    }


}


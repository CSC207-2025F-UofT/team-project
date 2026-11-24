package use_case.view_budget_progress;
import entity.Transaction;
import java.util.List;
public interface ViewBudgetProgressTransactionDataAccessInterface {
    String getCurrentUsername();
    List<Transaction> getTransactions(String username);
}

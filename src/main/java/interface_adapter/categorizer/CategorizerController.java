package interface_adapter.categorizer;

import entity.Transaction;
import use_case.transaction_categorizer.CategorizerInputBoundary;
import use_case.transaction_categorizer.TransactionCategorizerService;

import java.util.List;

public class CategorizerController {

    private CategorizerInputBoundary categorizerInputBoundary;

    public CategorizerController(CategorizerInputBoundary categorizerInputBoundary) {
        this.categorizerInputBoundary = categorizerInputBoundary;
    }

    public void categorizeTransactions(List<Transaction> transactions) {
        categorizerInputBoundary.generateCategories(transactions);
    }

    public void goToSpendingReport(String username) {
        categorizerInputBoundary.goToSpendingReport(username);
    }

    public void goBackToUploadStatement() {
        categorizerInputBoundary.goBackToUploadStatement();
    }

}

package interface_adapter.categorizer;

import java.util.List;

import entity.Transaction;
import use_case.transaction_categorizer.CategorizerInputBoundary;

public class CategorizerController {

    private final CategorizerInputBoundary categorizerInputBoundary;

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

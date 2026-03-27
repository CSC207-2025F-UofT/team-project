package use_case.transaction_categorizer;

import entity.Transaction;

import java.util.List;

public interface CategorizerInputBoundary {
    void generateCategories(List<Transaction> transactions);
    void goToSpendingReport(String username);

    void goBackToUploadStatement();

    }


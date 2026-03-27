package use_case.transaction_categorizer;

public interface CategorizerOutputBoundary {
    void displayCategorizationResults();
    void goToSpendingReport(String username);
    void goBackToUploadStatement();
}

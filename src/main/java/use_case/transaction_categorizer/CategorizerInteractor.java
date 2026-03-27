package use_case.transaction_categorizer;

import entity.Transaction;
import interface_adapter.categorizer.CategorizerViewModel;

import java.util.List;

public class CategorizerInteractor implements CategorizerInputBoundary {

    private TransactionCategorizerService  categorizerService;
    private CategorizerOutputBoundary categorizerOutputBoundary;

    public CategorizerInteractor(TransactionCategorizerService categorizerService,
                                 CategorizerOutputBoundary categorizerOutputBoundary) {
        this.categorizerService = categorizerService;
        this.categorizerOutputBoundary = categorizerOutputBoundary;
    }

    @Override
    public void generateCategories(List<Transaction> transactions) {
       categorizerService.categorize(transactions);
       categorizerOutputBoundary.displayCategorizationResults();
    }

    @Override
    public void goBackToUploadStatement() {
        categorizerOutputBoundary.goBackToUploadStatement();
    }

    @Override
    public void goToSpendingReport(String username) {
        categorizerOutputBoundary.goToSpendingReport(username);
    }
}

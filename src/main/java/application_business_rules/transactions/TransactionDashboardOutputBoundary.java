package application_business_rules.transactions;
public interface TransactionDashboardOutputBoundary {
    void present(TransactionDashboardOutputData outputData);
    void presentError(String message);
}

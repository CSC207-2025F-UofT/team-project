package use_case.transactions;
public interface TransactionDashboardOutputBoundary {
    void present(TransactionDashboardOutputData outputData);
    void presentError(String message);
}

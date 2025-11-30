package use_case.add_transaction;

public interface AddTransactionOutputBoundary {
    void prepareTransactionSuccessView (AddTransactionOutputData addTransactionOutputData);
    void prepareTransactionFailView (String errorMessage);
}

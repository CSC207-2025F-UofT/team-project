package use_case.upload_statement;

import entity.Transaction;

import java.util.List;

public interface UploadStatementOutputBoundary {
    void prepareSuccessView(UploadStatementOutputData outputData);
    void prepareFailView(String errorMessage);
    void prepareSpendingLimitsView();
    void prepareSpendingReportView();
    void prepareCategorizerView(List<Transaction> transactions);

    void prepareFilterSearchView();
}


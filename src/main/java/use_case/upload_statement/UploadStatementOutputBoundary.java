package use_case.upload_statement;

public interface UploadStatementOutputBoundary {
    void prepareSuccessView(UploadStatementOutputData outputData);
    void prepareFailView(String errorMessage);
    void prepareSpendingLimitsView();
}


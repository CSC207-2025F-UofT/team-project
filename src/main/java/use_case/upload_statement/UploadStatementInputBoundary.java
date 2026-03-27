package use_case.upload_statement;

public interface UploadStatementInputBoundary {
    void execute(UploadStatementInputData inputData);
    void goToSpendingLimits();
    void goToSpendingReport();
    void goToCategorizer(UploadStatementInputData inputData);

    void goToFilterSearch();
}

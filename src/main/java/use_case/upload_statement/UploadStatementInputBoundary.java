package use_case.upload_statement;

public interface UploadStatementInputBoundary {
    void execute(UploadStatementInputData inputData);
    void goToSpendingLimits();
}

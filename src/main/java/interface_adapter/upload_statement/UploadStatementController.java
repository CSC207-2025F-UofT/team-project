package interface_adapter.upload_statement;

import use_case.upload_statement.UploadStatementInputBoundary;
import use_case.upload_statement.UploadStatementInputData;

public class UploadStatementController {

    private final UploadStatementInputBoundary interactor;

    public UploadStatementController(UploadStatementInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String filePath, String username) {
        UploadStatementInputData data = new UploadStatementInputData(filePath, username);
        interactor.execute(data);
    }

    public void goToSpendingReport() {
        interactor.goToSpendingReport();
    }

    public void goToSpendingLimits() {
        interactor.goToSpendingLimits();
    }

    public void goToCategorizer(UploadStatementInputData inputData) {
        interactor.goToCategorizer(inputData);
    }

    public void goToFilterSearch() {
        interactor.goToFilterSearch();
    }
}

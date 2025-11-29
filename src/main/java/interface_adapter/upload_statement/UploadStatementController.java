package interface_adapter.upload_statement;

import use_case.upload_statement.UploadStatementInputBoundary;
import use_case.upload_statement.UploadStatementInputData;

public class UploadStatementController {
    private final UploadStatementInputBoundary interactor;

    public UploadStatementController(UploadStatementInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String filePath) {
        UploadStatementInputData data = new UploadStatementInputData(filePath);
        interactor.execute(data);
    }
}

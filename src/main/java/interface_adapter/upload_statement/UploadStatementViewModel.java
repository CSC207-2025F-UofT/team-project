package interface_adapter.upload_statement;

import interface_adapter.ViewModel;

public class UploadStatementViewModel extends ViewModel<UploadStatementState> {

    public UploadStatementViewModel() {
        super("statement view");
        setState(new UploadStatementState());
    }
}

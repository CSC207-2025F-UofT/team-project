package interface_adapter.upload_statement;

import interface_adapter.ViewManagerModel;
import use_case.upload_statement.UploadStatementOutputBoundary;
import use_case.upload_statement.UploadStatementOutputData;

public class UploadStatementPresenter implements UploadStatementOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final UploadStatementViewModel uploadStatementViewModel;

    public UploadStatementPresenter(ViewManagerModel viewManagerModel,
                                    UploadStatementViewModel uploadStatementViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.uploadStatementViewModel = uploadStatementViewModel;
    }

    @Override
    public void prepareSuccessView(UploadStatementOutputData outputData) {
        final UploadStatementState state = this.uploadStatementViewModel.getState();
        state.setTotalSpend(outputData.getTotalSpend());

        this.uploadStatementViewModel.firePropertyChange();

        // Switch to UploadStatementView
        this.viewManagerModel.setState(uploadStatementViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}

package interface_adapter.upload_statement;

import interface_adapter.ViewManagerModel;
import interface_adapter.spending_limits.SpendingLimitsState;
import interface_adapter.spending_limits.SpendingLimitsViewModel;
import use_case.upload_statement.UploadStatementOutputBoundary;
import use_case.upload_statement.UploadStatementOutputData;

public class UploadStatementPresenter implements UploadStatementOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final UploadStatementViewModel uploadStatementViewModel;
    private final SpendingLimitsViewModel spendingLimitsViewModel;

    public UploadStatementPresenter(ViewManagerModel viewManagerModel,
                                    UploadStatementViewModel uploadStatementViewModel,
                                    SpendingLimitsViewModel spendingLimitsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.uploadStatementViewModel = uploadStatementViewModel;
        this.spendingLimitsViewModel = spendingLimitsViewModel;
    }

    @Override
    public void prepareSuccessView(UploadStatementOutputData outputData) {
        final UploadStatementState state = this.uploadStatementViewModel.getState();
        state.setTotalSpend(outputData.getTotalSpend());
        state.setUsername(outputData.getUsername());
        this.uploadStatementViewModel.firePropertyChange();

        // Switch to UploadStatementView
        this.viewManagerModel.setState(uploadStatementViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        this.viewManagerModel.setState(uploadStatementViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareSpendingLimitsView() {
        final SpendingLimitsState spendingLimitsState = this.spendingLimitsViewModel.getState();
        spendingLimitsState.setUsername(this.uploadStatementViewModel.getState().getUsername());
        this.viewManagerModel.setState(spendingLimitsViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }
}

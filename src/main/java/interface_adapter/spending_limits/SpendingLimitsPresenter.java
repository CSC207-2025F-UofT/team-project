package interface_adapter.spending_limits;

import interface_adapter.ViewManagerModel;
import interface_adapter.upload_statement.UploadStatementViewModel;
import use_case.spending_limits.SpendingLimitsOutputBoundary;

import java.util.Map;

public class SpendingLimitsPresenter implements SpendingLimitsOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final SpendingLimitsViewModel spendingLimitsViewModel;
    private final UploadStatementViewModel uploadStatementViewModel;

    public SpendingLimitsPresenter(ViewManagerModel viewManagerModel,
            SpendingLimitsViewModel viewModel,
            UploadStatementViewModel uploadStatementViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.spendingLimitsViewModel = viewModel;
        this.uploadStatementViewModel = uploadStatementViewModel;
    }

    @Override
    public void prepareSuccessView() {
        spendingLimitsViewModel.firePropertyChange();
        viewManagerModel.setState(spendingLimitsViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
    }

    @Override
    public void presentLimits(Map<String, Double> limits) {

    }

    @Override
    public void presentSaveSuccess() {
        viewManagerModel.setState(uploadStatementViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void presentSaveFailure(String error) {
    }
}

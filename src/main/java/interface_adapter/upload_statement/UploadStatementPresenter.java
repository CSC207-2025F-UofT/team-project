package interface_adapter.upload_statement;

import java.util.List;

import entity.Transaction;
import interface_adapter.ViewManagerModel;
import interface_adapter.categorizer.CategorizerViewModel;
import interface_adapter.spending_limits.SpendingLimitsState;
import interface_adapter.spending_limits.SpendingLimitsViewModel;
import use_case.spending_report.SpendingReportViewModel;
import use_case.upload_statement.UploadStatementOutputBoundary;
import use_case.upload_statement.UploadStatementOutputData;

public class UploadStatementPresenter implements UploadStatementOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final UploadStatementViewModel uploadStatementViewModel;
    private final SpendingLimitsViewModel spendingLimitsViewModel;
    private final SpendingReportViewModel spendingReportViewModel;
    private final CategorizerViewModel categorizerViewModel;

    public UploadStatementPresenter(ViewManagerModel viewManagerModel,
            UploadStatementViewModel uploadStatementViewModel,
            SpendingLimitsViewModel spendingLimitsViewModel,
            SpendingReportViewModel spendingReportViewModel,
            CategorizerViewModel categorizerViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.uploadStatementViewModel = uploadStatementViewModel;
        this.spendingReportViewModel = spendingReportViewModel;
        this.spendingLimitsViewModel = spendingLimitsViewModel;
        this.categorizerViewModel = categorizerViewModel;
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

    @Override
    public void prepareSpendingReportView() {
        this.viewManagerModel.setState(spendingReportViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareCategorizerView(List<Transaction> transactions) {
        this.categorizerViewModel.setTransactions(transactions);
        this.viewManagerModel.setState("categorizer view");
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFilterSearchView() {
        viewManagerModel.setState("filter search");
        viewManagerModel.firePropertyChange();
    }
}

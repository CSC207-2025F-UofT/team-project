package interface_adapter.categorizer;

import interface_adapter.ViewManagerModel;
import use_case.spending_report.SpendingReportViewModel;
import use_case.transaction_categorizer.CategorizerOutputBoundary;

public class CategorizerPresenter implements CategorizerOutputBoundary {

    private CategorizerViewModel categorizerViewModel;
    private ViewManagerModel viewManagerModel;
    private SpendingReportViewModel spendingReportViewModel;

    public CategorizerPresenter(CategorizerViewModel categorizerViewModel,
            ViewManagerModel viewManagerModel, SpendingReportViewModel spendingReportViewModel) {
        this.categorizerViewModel = categorizerViewModel;
        this.viewManagerModel = viewManagerModel;
        this.spendingReportViewModel = spendingReportViewModel;
    }

    @Override
    public void displayCategorizationResults() {
        System.out.println("IN DISPLAY CATEGORIZATION RESULTS");
        this.categorizerViewModel.setState(categorizerViewModel.getState());
        this.categorizerViewModel.firePropertyChange("state");
    }

    @Override
    public void goToSpendingReport(String username) {
        this.spendingReportViewModel.setUsername(username);
        this.viewManagerModel.setState("spending report");
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void goBackToUploadStatement() {
        viewManagerModel.setState("statement view");
        viewManagerModel.firePropertyChange();
    }

}

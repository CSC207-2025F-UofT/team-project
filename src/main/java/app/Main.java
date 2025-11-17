package app;

import data_access.CSVTransactionDAO;
import entity.SpendingReportViewModel;
import interface_adapter.TransactionDataAccess;
import use_case.spending_report.GenerateReportController;
import use_case.spending_report.GenerateReportInteractor;
import use_case.spending_report.GenerateReportPresenter;

public class Main {
    public static void main(String[] args) {
        TransactionDataAccess transactionDAO = new CSVTransactionDAO("transactions.csv");
        SpendingReportViewModel viewModel = new SpendingReportViewModel();
        GenerateReportPresenter presenter = new GenerateReportPresenter(viewModel);
        GenerateReportInteractor interactor = new GenerateReportInteractor(transactionDAO, presenter);
        GenerateReportController controller = new GenerateReportController(interactor);
        viewModel.setController(controller);
        viewModel.setVisible(true);
    }
}
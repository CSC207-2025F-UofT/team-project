package app;

import data_access.CSVTransactionDAO; // Fixed import
import entity.SpendingReportViewModel;
import interface_adapter.TransactionDataAccess;
import use_case.spending_report.GenerateReportController;
import use_case.spending_report.GenerateReportInteractor;
import use_case.spending_report.GenerateReportPresenter;

public class Main {
    public static void main(String[] args) {
        // Use CSV data access instead of in-memory
        TransactionDataAccess transactionDAO = new CSVTransactionDAO("transactions.csv");
        
        // Rest of your setup remains the same
        SpendingReportViewModel viewModel = new SpendingReportViewModel(null);
        
        GenerateReportPresenter presenter = new GenerateReportPresenter(viewModel);
        GenerateReportInteractor interactor = new GenerateReportInteractor(transactionDAO, presenter);
        GenerateReportController controller = new GenerateReportController(interactor);
                
        viewModel = new SpendingReportViewModel(controller);
        
        viewModel.setVisible(true);
    }
}
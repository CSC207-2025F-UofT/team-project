package app;

import data_access.CSVTransactionDAO;
import entity.SpendingReportViewModel;
import interface_adapter.TransactionDataAccess;
import use_case.spending_report.GenerateReportController;
import use_case.spending_report.GenerateReportInteractor;
import use_case.spending_report.GenerateReportPresenter;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting application...");
        
        // 1. Create DAO
        TransactionDataAccess transactionDAO = new CSVTransactionDAO("transactions.csv");
        System.out.println("DAO created");
        
        // 2. Create viewModel WITHOUT controller
        SpendingReportViewModel viewModel = new SpendingReportViewModel();
        System.out.println("ViewModel created");
        
        // 3. Create presenter with the viewModel
        GenerateReportPresenter presenter = new GenerateReportPresenter(viewModel);
        System.out.println("Presenter created");
        
        // 4. Create interactor
        GenerateReportInteractor interactor = new GenerateReportInteractor(transactionDAO, presenter);
        System.out.println("Interactor created");
        
        // 5. Create controller
        GenerateReportController controller = new GenerateReportController(interactor);
        System.out.println("Controller created");
        
        // 6. NOW set the controller in viewModel
        viewModel.setController(controller);
        System.out.println("Controller set in viewModel");
        
        // 7. Display UI
        viewModel.setVisible(true);
        System.out.println("UI displayed");
    }
}
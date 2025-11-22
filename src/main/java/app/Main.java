package app;

import javax.swing.SwingUtilities;

import data_access.CSVTransactionDAO;
import interface_adapter.TransactionDataAccess;
import use_case.spending_report.GenerateReportController;
import use_case.spending_report.GenerateReportInteractor;
import use_case.spending_report.GenerateReportPresenter;
import use_case.spending_report.SpendingReportView;
import use_case.spending_report.SpendingReportViewModel;

public class Main {
    public static void main(String[] args) {
        TransactionDataAccess transactionDAO = new CSVTransactionDAO("transactions.csv");
        SpendingReportViewModel viewModel = new SpendingReportViewModel();
        SpendingReportView view = new SpendingReportView();
        GenerateReportPresenter presenter = new GenerateReportPresenter(view);
        GenerateReportInteractor interactor = new GenerateReportInteractor(transactionDAO, presenter);
        GenerateReportController controller = new GenerateReportController(interactor);
        
        view.addMonthDropdownListener(e -> {
            String selectedMonth = (String) view.getMonthDropdown().getSelectedItem();
            String chartType = (String) view.getChartTypeDropdown().getSelectedItem();
            viewModel.setChartType(chartType);
            controller.generateReport(1, selectedMonth);
        });
        
        view.addChartTypeDropdownListener(e -> {
            String selectedMonth = (String) view.getMonthDropdown().getSelectedItem();
            String chartType = (String) view.getChartTypeDropdown().getSelectedItem();
            viewModel.setChartType(chartType);
            controller.generateReport(1, selectedMonth);
        });

        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
            view.setInitialMonth("November 2025");
            controller.generateReport(1, "November 2025");
        });
    }
}
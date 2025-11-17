package use_case.spending_report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.SpendingReport;
import entity.Transaction;
import interface_adapter.TransactionDataAccess;

public class GenerateReportInteractor implements GenerateReportInputBoundary {
    private final TransactionDataAccess transactionDAO;
    private final GenerateReportOutputBoundary presenter;

    public GenerateReportInteractor(TransactionDataAccess transactionDAO,
                                    GenerateReportOutputBoundary presenter) {
        this.transactionDAO = transactionDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateReportInput inputData) {
        List<Transaction> transactions = transactionDAO.getTransactions(
                inputData.getUserId(), inputData.getMonth());

        if (transactions.isEmpty()) {
            presenter.presentReport(new GenerateReportOutput(null, false));
            return;
        }

        Map<String, Float> categoryTotals = new HashMap<>();
        for (Transaction t : transactions) {
            categoryTotals.merge(t.getCategory(), t.getAmount(), Float::sum);
        }

        SpendingReport report = new SpendingReport(inputData.getMonth(), categoryTotals);
        presenter.presentReport(new GenerateReportOutput(report, true));
    }
}
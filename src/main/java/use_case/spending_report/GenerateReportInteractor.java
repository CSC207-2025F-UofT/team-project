package use_case.spending_report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data_access.CSVTransactionDAO;
import entity.SpendingReport;
import entity.Transaction;

public class GenerateReportInteractor implements GenerateReportInputBoundary {

    private final CSVTransactionDAO transactionDAO;
    private final GenerateReportOutputBoundary presenter;

    public GenerateReportInteractor(CSVTransactionDAO transactionDAO,
            GenerateReportOutputBoundary presenter) {
        this.transactionDAO = transactionDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateReportInput inputData) {
        List<Transaction> transactions = transactionDAO.getTransactions(inputData.getUsername());
        if (transactions.isEmpty()) {
            presenter.presentReport(new GenerateReportOutput(null, false));
            return;
        }

        Map<String, Float> categoryTotals = new HashMap<>();
        for (Transaction t : transactions) {
            String category = t.getCategory();
            Float amount = (float) t.getAmount();

            if (categoryTotals.containsKey(category)) {
                categoryTotals.put(category, categoryTotals.get(category) + amount);
            } else {
                categoryTotals.put(category, amount);
            }
        }

        SpendingReport report = new SpendingReport(categoryTotals);
        presenter.presentReport(new GenerateReportOutput(report, true));
    }

    @Override
    public void backToCategorizeView() {
        presenter.backToCategorizeView();
    }
}

package use_case.spending_report;

import java.util.*;

public class GenerateReportInteractor implements GenerateReportInputBoundary {
    private final TransactionDataAccessInterface transactionDAO;
    private final GenerateReportOutputBoundary presenter;

    public GenerateReportInteractor(TransactionDataAccessInterface transactionDAO,
                                    GenerateReportOutputBoundary presenter) {
        this.transactionDAO = transactionDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateReportInputData inputData) {
        List<Transaction> transactions = transactionDAO.getTransactionsByUserAndMonth(
                inputData.getUserId(), inputData.getMonth());

        if (transactions.isEmpty()) {
            presenter.present(new GenerateReportOutput(null, false));
            return;
        }

        Map<String, Float> categoryTotals = new HashMap<>();
        for (Transaction t : transactions) {
            categoryTotals.merge(t.getCategory(), t.getAmount(), Float::sum);
        }

        Report report = new Report(inputData.getMonth(), categoryTotals);
        presenter.present(new GenerateReportOutput(report, true));
    }
}

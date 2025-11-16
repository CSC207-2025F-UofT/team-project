import java.util.*;
import entity.transaction.Transaction;
import use_case.transaction.TransactionDataAccessInterface;
import use_case.spending_report.GenerateReportInputBoundary;
import use_case.spending_report.GenerateReportInput;
import use_case.spending_report.GenerateReportOutputBoundary;
import use_case.spending_report.GenerateReportOutputData;
import entity.report.Report;

public class GenerateReportInteractor implements GenerateReportInputBoundary {
    private final TransactionDataAccessInterface transactionDAO;
    private final GenerateReportOutputBoundary presenter;

    public GenerateReportInteractor(TransactionDataAccessInterface transactionDAO,
                                    GenerateReportOutputBoundary presenter) {
        this.transactionDAO = transactionDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateReportInput inputData) {
        List<Transaction> transactions = transactionDAO.getTransactionsByUserAndMonth(
                inputData.getUserId(), inputData.getMonth());

        if (transactions.isEmpty()) {
            presenter.present(new GenerateReportOutputData(null, false));
            return;
        }

        Map<String, Float> categoryTotals = new HashMap<>();
        for (Transaction t : transactions) {
            categoryTotals.merge(t.getCategory(), t.getAmount(), Float::sum);
        }

        Report report = new Report(inputData.getMonth(), categoryTotals);
        presenter.present(new GenerateReportOutputData(report, true));
    }
}

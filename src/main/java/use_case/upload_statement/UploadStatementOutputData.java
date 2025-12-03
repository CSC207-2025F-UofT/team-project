package use_case.upload_statement;

import java.util.Map;
import java.util.List;

public class UploadStatementOutputData {

    private final int numTransactionsImported;
    private final boolean success;
    private final String message;
    private final double totalSpend;

    private final Map<String, Double> categoryTotals;
    private final List<String> alerts;
    private final String username;

    public UploadStatementOutputData(
            int numTransactionsImported,
            boolean success,
            String message,
            Map<String, Double> categoryTotals,
            List<String> alerts,
            double totalSpend,
            String username
    ) {
        this.numTransactionsImported = numTransactionsImported;
        this.success = success;
        this.message = message;
        this.categoryTotals = categoryTotals;
        this.alerts = alerts;
        this.totalSpend = totalSpend;
        this.username = username;
    }

    public int getNumTransactionsImported() { return numTransactionsImported; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Map<String, Double> getCategoryTotals() { return categoryTotals; }
    public List<String> getAlerts() { return alerts; }
    public double getTotalSpend() { return totalSpend; }
    public String getUsername() { return username; }
}
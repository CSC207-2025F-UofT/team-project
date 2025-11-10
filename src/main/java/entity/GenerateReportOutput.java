package entity;

public class GenerateReportOutput {
    private final SpendingReport report;
    private final boolean success;

    public GenerateReportOutput(SpendingReport report, boolean success) {
        this.report = report;
        this.success = success;
    }

    public SpendingReport getReport() { return report; }
    public boolean isSuccess() { return success; }
}

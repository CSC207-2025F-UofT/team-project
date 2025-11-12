package use_case.spending_report;

public class GenerateReportInput {
    private final String month;
    private final int userId;

    public GenerateReportInput(int userId, String month) {
        this.userId = userId;
        this.month = month;
    }

    public int getUserId() { return userId; }
    public String getMonth() { return month; }
}

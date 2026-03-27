package use_case.spending_report;

public class GenerateReportInput {
    private final String username;

    public GenerateReportInput(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
}

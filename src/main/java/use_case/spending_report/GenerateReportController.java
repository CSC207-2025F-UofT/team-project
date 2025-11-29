package use_case.spending_report;

public class GenerateReportController {
    private final GenerateReportInteractor interactor;

    public GenerateReportController(GenerateReportInteractor interactor) {
        this.interactor = interactor;
    }

    public void generateReport(int userId, String month) {
        GenerateReportInput inputData = new GenerateReportInput(userId, month);
        interactor.execute(inputData);
    }
}
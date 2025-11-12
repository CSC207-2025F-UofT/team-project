package use_case.spending_report;

public class GenerateReportController {
    private final GenerateReportInputBoundary interactor;

    public GenerateReportController(GenerateReportInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void onGenerateReportClicked(int userId, String month) {
        GenerateReportInputData inputData = new GenerateReportInput(userId, month);
        interactor.execute(inputData);
    }
}

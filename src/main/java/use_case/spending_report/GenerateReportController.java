package use_case.spending_report;

public class GenerateReportController {
    private final GenerateReportInteractor interactor;

    public GenerateReportController(GenerateReportInteractor interactor) {
        this.interactor = interactor;
    }

    public void generateReport(String username) {
        GenerateReportInput inputData = new GenerateReportInput(username);
        interactor.execute(inputData);
    }

    public void backToCategorizeView() {
        interactor.backToCategorizeView();
    }
}
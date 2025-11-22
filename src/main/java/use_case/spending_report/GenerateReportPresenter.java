package use_case.spending_report;

public class GenerateReportPresenter implements GenerateReportOutputBoundary {
    private final SpendingReportView view;

    public GenerateReportPresenter(SpendingReportView view) {
        this.view = view;
    }

    @Override
    public void presentReport(GenerateReportOutput outputData) {
        if (outputData.isSuccess()) {
            view.displayChart(outputData.getReport().getCategoryBreakdown(), 
                                 outputData.getReport().getMonth());
        } else {
            view.displayChart(null, "");
        }
    }
}
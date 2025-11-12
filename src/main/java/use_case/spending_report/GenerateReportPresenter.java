package use_case.spending_report;

public class GenerateReportPresenter implements GenerateReportOutputBoundary {
    private final SpendingReportViewModel viewModel;

    public GenerateReportPresenter(SpendingReportViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GenerateReportOutput outputData) {
        if (outputData.isSuccess()) {
            viewModel.setReport(outputData.getReport());
            viewModel.displayChart();
        } else {
            viewModel.displayNoDataMessage();
        }
    }
}

package use_case.spending_report;
import entity.SpendingReportViewModel;

public class GenerateReportPresenter implements GenerateReportOutputBoundary {
    private final SpendingReportViewModel viewModel;

    public GenerateReportPresenter(SpendingReportViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentReport(GenerateReportOutput outputData) {
        if (outputData.isSuccess()) {
            viewModel.setReport(outputData.getReport());
            viewModel.displayChart();
        } else {
            viewModel.displayNoDataMessage();
        }
    }
}

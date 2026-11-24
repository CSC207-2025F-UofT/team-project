package interface_adapter.job_filter;

import java.util.List;
import use_case.job_filter.JobFilterOutputBoundary;
import use_case.job_filter.JobFilterOutputData;

public class JobFilterPresenter implements JobFilterOutputBoundary {
    private final JobFilterViewModel viewModel;

    public JobFilterPresenter(JobFilterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(JobFilterOutputData outputData) {
        publish(new JobFilterState(outputData.getJobs(), null));
    }

    @Override
    public void prepareFailView(String errorMessage) {
        publish(new JobFilterState(List.of(), errorMessage));
    }

    private void publish(JobFilterState state) {
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}

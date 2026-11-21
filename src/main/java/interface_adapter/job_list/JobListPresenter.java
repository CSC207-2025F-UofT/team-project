package interface_adapter.job_list;

import interface_adapter.ViewManagerModel;
import use_case.job_match.JobMatchOutputBoundary;
import use_case.job_match.JobMatchOutputData;

/**
 * The Presenter for the Job Match Use Case.
 */
public class JobListPresenter implements JobMatchOutputBoundary {

    private final JobListViewModel jobListViewModel;
    private final ViewManagerModel viewManagerModel;

    public JobListPresenter(ViewManagerModel viewManagerModel,
                            JobListViewModel jobListViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.jobListViewModel = jobListViewModel;
    }

    @Override
    public void prepareSuccessView(JobMatchOutputData response) {
        final JobListState jobListState = jobListViewModel.getState();
        jobListState.setJobs(response.getJobs());
        jobListState.setError(null);
        jobListViewModel.notifyStateChanged();

        viewManagerModel.setState(JobListViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final JobListState jobListState = jobListViewModel.getState();
        jobListState.setError(errorMessage);
        jobListViewModel.notifyStateChanged();
    }
}

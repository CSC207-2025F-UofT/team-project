package interface_adapter.job_list;

import java.util.ArrayList;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
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
        jobListState.setJobs(new ArrayList<>());
        jobListState.setError(errorMessage);
        jobListViewModel.notifyStateChanged();

        // Still switch to the job list so the student can see the error.
        viewManagerModel.setState(JobListViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void switchToLoggedInView() {
        viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}

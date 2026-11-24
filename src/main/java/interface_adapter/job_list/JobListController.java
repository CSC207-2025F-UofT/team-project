package interface_adapter.job_list;

import use_case.job_match.JobMatchInputBoundary;
import use_case.job_match.JobMatchInputData;

/**
 * Controller for the Job Match Use Case.
 */
public class JobListController {

    private final JobMatchInputBoundary jobMatchUseCaseInteractor;

    public JobListController(JobMatchInputBoundary jobMatchUseCaseInteractor) {
        this.jobMatchUseCaseInteractor = jobMatchUseCaseInteractor;
    }

    /**
     * Executes the Job Match Use Case.
     * @param username the logged-in student whose profile is used for matching
     */
    public void execute(String username) {
        jobMatchUseCaseInteractor.execute(new JobMatchInputData(username));
    }

    /**
     * Executes the "switch to LoggedInView" Use Case.
     */
    public void switchToLoggedInView() {
        jobMatchUseCaseInteractor.switchToLoggedInView();
    }
}

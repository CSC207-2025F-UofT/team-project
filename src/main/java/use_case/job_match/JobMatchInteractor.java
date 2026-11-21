package use_case.job_match;

import entity.Job;
import entity.Student;

import java.util.ArrayList;

/**
 * The Job Match Interactor.
 */
public class JobMatchInteractor implements JobMatchInputBoundary {
    private final JobMatchUserDataAccessInterface jobDataAccessObject;
    private final JobMatchStudentDataAccessInterface studentDataAccessObject;
    private final JobMatchOutputBoundary userPresenter;

    public JobMatchInteractor(JobMatchUserDataAccessInterface jobMatchDataAccessInterface,
                              JobMatchStudentDataAccessInterface studentDataAccessInterface,
                              JobMatchOutputBoundary jobMatchOutputBoundary) {
        this.jobDataAccessObject = jobMatchDataAccessInterface;
        this.studentDataAccessObject = studentDataAccessInterface;
        this.userPresenter = jobMatchOutputBoundary;
    }

    @Override
    public void execute(JobMatchInputData jobMatchInputData) {
        final Student student = studentDataAccessObject.get(jobMatchInputData.getUsername());
        if (student == null) {
            userPresenter.prepareFailView("Please log in first.");
            return;
        }
        final ArrayList<Job> jobs = jobDataAccessObject.getJobs(student);
        if (jobs.isEmpty()) {
            userPresenter.prepareFailView("No matching jobs found.");
            return;
        }
        userPresenter.prepareSuccessView(new JobMatchOutputData(jobs));
    }

    @Override
    public void switchToLoggedInView() {
        userPresenter.switchToLoggedInView();
    }
}

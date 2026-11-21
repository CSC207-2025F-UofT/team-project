package use_case.job_match;

import entity.Job;
import entity.Student;

import java.util.ArrayList;

/**
 * The Job Match Interactor.
 */
public class JobMatchInteractor implements JobMatchInputBoundary {
    private final JobMatchUserDataAccessInterface userDataAccessObject;
    private final JobMatchOutputBoundary userPresenter;

    public JobMatchInteractor(JobMatchUserDataAccessInterface jobMatchDataAccessInterface,
                              JobMatchOutputBoundary jobMatchOutputBoundary) {
        this.userDataAccessObject = jobMatchDataAccessInterface;
        this.userPresenter = jobMatchOutputBoundary;
    }

    @Override
    public void execute(JobMatchInputData jobMatchInputData) {
        final Student student = jobMatchInputData.getStudent();
        final ArrayList<Job> jobs = userDataAccessObject.getJobs(student);
        final JobMatchOutputData jobMatchOutputData = new JobMatchOutputData(jobs);
        userPresenter.prepareSuccessView(jobMatchOutputData);
    }
}

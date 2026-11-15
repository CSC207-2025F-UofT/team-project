package use_case.job_match;

import entity.Student;
import entity.StudentFactory;

/**
 * The Job Match Interactor.
 */
public class JobMatchInteractor implements JobMatchInputBoundary {
    private final JobMatchUserDataAccessInterface userDataAccessObject;
    private final JobMatchOutputBoundary userPresenter;
    private final StudentFactory studentFactory;

    public JobMatchInteractor(JobMatchUserDataAccessInterface jobMatchDataAccessInterface,
                                    JobMatchOutputBoundary jobMatchOutputBoundary,
                                    StudentFactory studentFactory) {
        this.userDataAccessObject = jobMatchDataAccessInterface;
        this.userPresenter = jobMatchOutputBoundary;
        this.studentFactory = studentFactory;
    }

    @Override
    public void execute(JobMatchInputData jobMatchInputData) {
        if ("".equals(jobMatchInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else {
            final Student student = studentFactory.create(jobMatchInputData.getUsername(),
                    jobMatchInputData.getPassword());

            userDataAccessObject.changePassword(student);

            final JobMatchOutputData jobMatchOutputData = new JobMatchOutputData(student.getName());
            userPresenter.prepareSuccessView(jobMatchOutputData);
        }
    }
}

package interface_adapter.job_list;

import entity.Student;
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
     * @param student the student whose profile is used for matching
     */
    public void execute(Student student) {
        final JobMatchInputData jobMatchInputData = new JobMatchInputData(student);
        jobMatchUseCaseInteractor.execute(jobMatchInputData);
    }
}

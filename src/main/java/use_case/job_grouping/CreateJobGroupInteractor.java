package use_case.job_grouping;

import entity.JobGroup;
import entity.Student;

/**
 * The Create Job Group Interactor.
 */
public class CreateJobGroupInteractor implements CreateJobGroupInputBoundary {

    private final CreateJobGroupDataAccessInterface userDataAccessObject;
    private final CreateJobGroupOutputBoundary userPresenter;

    /**
     * Creates the Create Job Group Interactor.
     * @param userDataAccessObject the data access object
     * @param userPresenter the output boundary
     */
    public CreateJobGroupInteractor(
            CreateJobGroupDataAccessInterface userDataAccessObject,
            CreateJobGroupOutputBoundary userPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
    }

    @Override
    public void execute(CreateJobGroupInputData createJobGroupInputData) {

        if (createJobGroupInputData.getGroupName() == null
                || "".equals(createJobGroupInputData.getGroupName().trim())) {
            userPresenter.prepareFailView("Group name cannot be empty");
        }
        else if (createJobGroupInputData.getJobs().isEmpty()) {
            userPresenter.prepareFailView("Select at least one job");
        }
        else {
            final Student student = userDataAccessObject.getStudent(
                    createJobGroupInputData.getUsername());

            if (student == null) {
                userPresenter.prepareFailView("Student not found");
                return;
            }

            final JobGroup jobGroup = new JobGroup(
                    createJobGroupInputData.getGroupName(),
                    createJobGroupInputData.getJobs());

            student.addJobGroup(jobGroup);
            userDataAccessObject.save(student);

            final CreateJobGroupOutputData outputData =
                    new CreateJobGroupOutputData(jobGroup.getName());

            userPresenter.prepareSuccessView(outputData);
        }
    }
}

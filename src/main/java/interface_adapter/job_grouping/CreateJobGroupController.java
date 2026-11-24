package interface_adapter.job_grouping;

import entity.Job;
import use_case.job_grouping.CreateJobGroupInputBoundary;
import use_case.job_grouping.CreateJobGroupInputData;

import java.util.ArrayList;

/**
 * The controller for the Create Job Group use case.
 */
public class CreateJobGroupController {

    private final CreateJobGroupInputBoundary createJobGroupInteractor;

    /**
     * Creates the Create Job Group Controller.
     * @param createJobGroupInteractor the input boundary
     */
    public CreateJobGroupController(
            CreateJobGroupInputBoundary createJobGroupInteractor) {
        this.createJobGroupInteractor = createJobGroupInteractor;
    }

    /**
     * Executes the Create Job Group use case.
     * @param username the student's username
     * @param groupName the name of the new group
     * @param jobs the selected jobs
     */
    public void execute(String username, String groupName, ArrayList<Job> jobs) {
        final CreateJobGroupInputData inputData =
                new CreateJobGroupInputData(username, groupName, jobs);

        createJobGroupInteractor.execute(inputData);
    }
}

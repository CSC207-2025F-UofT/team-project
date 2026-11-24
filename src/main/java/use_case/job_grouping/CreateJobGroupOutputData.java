package use_case.job_grouping;

/**
 * The output data for the Create Job Group use case.
 */
public class CreateJobGroupOutputData {

    private final String groupName;

    /**
     * Creates the output data for a successfully created job group.
     * @param groupName the name of the created group
     */
    public CreateJobGroupOutputData(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}

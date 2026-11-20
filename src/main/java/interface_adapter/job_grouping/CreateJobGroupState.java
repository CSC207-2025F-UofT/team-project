package interface_adapter.job_grouping;

/**
 * The state for the Create Job Group View Model.
 */
public class CreateJobGroupState {

    private String groupName = "";
    private String errorMessage;
    private boolean success;

    public String getGroupName() {
        return groupName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

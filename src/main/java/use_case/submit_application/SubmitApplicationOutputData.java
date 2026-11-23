package use_case.submit_application;

/**
 * Output Data for the Submit Application Use Case.
 */
public final class SubmitApplicationOutputData {

    private final String applicationId;
    private final String petName;

    /**
     * Creates the output data.
     * @param applicationId the id of the saved application
     * @param petName the name of the pet applied for
     */
    public SubmitApplicationOutputData(String applicationId, String petName) {
        this.applicationId = applicationId;
        this.petName = petName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getPetName() {
        return petName;
    }
}

package use_case.submit_application;

/**
 * Output Data for the Submit Application Use Case.
 */
public class SubmitApplicationOutputData {

    private final String applicationId;
    private final String petName;

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

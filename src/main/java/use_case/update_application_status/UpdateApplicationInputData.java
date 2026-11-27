package use_case.update_application_status;

public class UpdateApplicationInputData {
    private final String applicationId;

    public UpdateApplicationInputData(String applicationId) {
        this.applicationId = applicationId;
    }

    String getApplicationId() {return applicationId;}
}

package use_case.update_application_status;

import entity.ApplicationStatus;

public class UpdateApplicationOutputData {
    private final String applicationId;
    private final ApplicationStatus applicationStatus;
    private final String adminNote;

    public UpdateApplicationOutputData(String applicationId, ApplicationStatus applicationStatus, String adminNote) {
        this.applicationId = applicationId;
        this.applicationStatus = applicationStatus;
        this.adminNote = adminNote;
    }

    public String getApplicationId() {return applicationId;}

    public ApplicationStatus getApplicationStatus() {return applicationStatus;}

    public String getAdminNote() {return adminNote;}
}

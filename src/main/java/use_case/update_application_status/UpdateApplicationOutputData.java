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

    String getApplicationId() {return applicationId;}

    ApplicationStatus getApplicationStatus() {return applicationStatus;}

    String getAdminNote() {return adminNote;}
}

package use_case.update_application_status;

import entity.ApplicationStatus;

/**
 * The admin's decision on one application.
 */
public class UpdateApplicationInputData {
    private final String applicationId;
    private final ApplicationStatus newStatus;
    private final String adminNote;

    /**
     * Creates the input.
     * @param applicationId the application the admin is changing
     * @param newStatus the status the admin chose
     * @param adminNote a note for the adopter, may be empty
     */
    public UpdateApplicationInputData(String applicationId, ApplicationStatus newStatus, String adminNote) {
        this.applicationId = applicationId;
        this.newStatus = newStatus;
        this.adminNote = adminNote;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public ApplicationStatus getNewStatus() {
        return newStatus;
    }

    public String getAdminNote() {
        return adminNote;
    }
}

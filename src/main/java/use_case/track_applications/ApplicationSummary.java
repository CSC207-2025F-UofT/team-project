package use_case.track_applications;

import java.time.LocalDate;

/**
 * One application as shown to the adopter who submitted it.
 */
public class ApplicationSummary {

    private final String applicationId;
    private final String petName;
    private final LocalDate submittedAt;
    private final String status;
    private final String adminNote;

    /**
     * Creates a summary of one application.
     * @param applicationId the application's id
     * @param petName the name of the pet applied for
     * @param submittedAt the day the application was submitted
     * @param status the current status, for example PENDING
     * @param adminNote the shelter's note, empty if there is none
     */
    public ApplicationSummary(String applicationId, String petName, LocalDate submittedAt, String status,
                              String adminNote) {
        this.applicationId = applicationId;
        this.petName = petName;
        this.submittedAt = submittedAt;
        this.status = status;
        this.adminNote = adminNote;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getPetName() {
        return petName;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getAdminNote() {
        return adminNote;
    }
}

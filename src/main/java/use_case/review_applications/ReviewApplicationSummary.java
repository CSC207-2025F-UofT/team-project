package use_case.review_applications;

import java.time.LocalDate;

/**
 * One application as shown in the admin's list.
 */
public final class ReviewApplicationSummary {

    private final String applicationId;
    private final String petId;
    private final String petName;
    private final String adopterName;
    private final LocalDate submittedAt;
    private final String status;

    /**
     * Creates a summary of one application.
     * @param applicationId the application's id
     * @param petId the id of the pet applied for
     * @param petName the name of the pet applied for
     * @param adopterName the name of the adopter who applied
     * @param submittedAt the day the application was submitted
     * @param status the current status, for example PENDING
     */
    public ReviewApplicationSummary(String applicationId, String petId, String petName, String adopterName,
                                    LocalDate submittedAt, String status) {
        this.applicationId = applicationId;
        this.petId = petId;
        this.petName = petName;
        this.adopterName = adopterName;
        this.submittedAt = submittedAt;
        this.status = status;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getPetId() {
        return petId;
    }

    public String getPetName() {
        return petName;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public String getStatus() {
        return status;
    }
}

package use_case.review_applications;

import entity.ApplicationStatus;

/**
 * The filters the admin chose. A filter left empty does not restrict the list.
 */
public class ReviewApplicationsInputData {
    private final ApplicationStatus status;
    private final String petId;

    /**
     * Creates the input.
     * @param status only show applications with this status, or null for every status
     * @param petId only show applications for this pet, or null or empty for every pet
     */
    public ReviewApplicationsInputData(ApplicationStatus status, String petId) {
        this.status = status;
        this.petId = petId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getPetId() {
        return petId;
    }
}

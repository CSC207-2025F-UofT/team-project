package interface_adapter.review_applications;

import entity.ApplicationStatus;
import use_case.review_applications.ReviewApplicationsInputBoundary;
import use_case.review_applications.ReviewApplicationsInputData;

/**
 * The controller for the Review Applications Use Case.
 */
public class ReviewApplicationsController {

    private final ReviewApplicationsInputBoundary reviewApplicationsInteractor;

    /**
     * Creates the controller.
     * @param reviewApplicationsInteractor the interactor that handles the request
     */
    public ReviewApplicationsController(ReviewApplicationsInputBoundary reviewApplicationsInteractor) {
        this.reviewApplicationsInteractor = reviewApplicationsInteractor;
    }

    /**
     * Lists the applications, filtered by what the admin chose.
     * @param petId only show applications for this pet; null or empty for every pet
     * @param status one of {@link ReviewApplicationsViewModel#STATUS_OPTIONS};
     *               null, empty or {@link ReviewApplicationsViewModel#ALL_STATUSES} for every status
     * @throws IllegalArgumentException if the status is not one of the options
     */
    public void execute(String petId, String status) {
        reviewApplicationsInteractor.execute(new ReviewApplicationsInputData(toStatus(status), petId));
    }

    /**
     * Opens one application to read the adopter's answers.
     * @param applicationId the application the admin chose
     */
    public void viewApplication(String applicationId) {
        reviewApplicationsInteractor.viewApplication(applicationId);
    }

    private static ApplicationStatus toStatus(String status) {
        if (status == null || status.isEmpty() || ReviewApplicationsViewModel.ALL_STATUSES.equals(status)) {
            return null;
        }
        return ApplicationStatus.valueOf(status);
    }
}

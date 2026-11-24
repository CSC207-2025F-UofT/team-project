package use_case.review_applications;

/**
 * Output boundary for the Review Applications use case.
 */
public interface ReviewApplicationsOutputBoundary {

    /**
     * Shows the matching applications.
     * @param reviewApplicationsOutputData the applications to show, never empty
     */
    void prepareSuccessView(ReviewApplicationsOutputData reviewApplicationsOutputData);

    /**
     * Shows one application with the adopter's answers.
     * @param applicationDetailsOutputData the chosen application
     */
    void prepareDetailView(ApplicationDetailsOutputData applicationDetailsOutputData);

    /**
     * Tells the admin why the applications, or the chosen application, cannot be shown.
     * @param errorMessage the message to show
     */
    void prepareFailView(String errorMessage);
}

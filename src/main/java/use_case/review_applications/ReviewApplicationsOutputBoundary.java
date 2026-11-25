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
     * Tells the admin why no applications are listed.
     * @param errorMessage the message to show
     */
    void prepareFailView(String errorMessage);

    /**
     * Tells the admin why the chosen application cannot be shown. The list stays as it is.
     * @param errorMessage the message to show
     */
    void prepareDetailFailView(String errorMessage);
}

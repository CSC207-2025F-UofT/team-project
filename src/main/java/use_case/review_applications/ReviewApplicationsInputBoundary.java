package use_case.review_applications;

/**
 * Input boundary for the Review Applications use case.
 */
public interface ReviewApplicationsInputBoundary {

    /**
     * Lists the applications that match the given filters.
     * @param reviewApplicationsInputData the filters; empty filters show every application
     */
    void execute(ReviewApplicationsInputData reviewApplicationsInputData);

    /**
     * Shows one application with the adopter's answers.
     * @param applicationId the application the admin chose
     */
    void viewApplication(String applicationId);
}

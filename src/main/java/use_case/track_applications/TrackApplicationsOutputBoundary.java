package use_case.track_applications;

/**
 * The Output Boundary for the Track Applications Use Case.
 */
public interface TrackApplicationsOutputBoundary {

    /**
     * Prepares the view that lists the adopter's applications.
     * @param outputData the output data
     */
    void prepareSuccessView(TrackApplicationsOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}

package use_case.track_score;

/**
 * Output Boundary for the Track Score use case.
 */
public interface TrackScoreOutputBoundary {
    /**
     * Prepares the success view for the Track Score use case.
     * @param outputData the output data
     */
    void prepareSuccessView(TrackScoreOutputData outputData);

    /**
     * Prepares the failure view for the Track Score use case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
package use_case.track_score;

/**
 * Input Boundary for the Track Score use case.
 */
public interface TrackScoreInputBoundary {
    /**
     * Executes the Track Score use case.
     * @param trackScoreInputData the input data
     */
    void execute(TrackScoreInputData trackScoreInputData);
}
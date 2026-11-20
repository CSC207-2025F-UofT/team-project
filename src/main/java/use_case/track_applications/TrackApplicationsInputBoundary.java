package use_case.track_applications;

/**
 * Input Boundary for the Track Applications Use Case.
 */
public interface TrackApplicationsInputBoundary {

    /**
     * Executes the track applications use case.
     * @param inputData the input data
     */
    void execute(TrackApplicationsInputData inputData);
}

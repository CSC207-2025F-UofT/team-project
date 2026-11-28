package interface_adapter.track_applications;

import use_case.track_applications.TrackApplicationsInputBoundary;
import use_case.track_applications.TrackApplicationsInputData;

/**
 * The controller for the Track Applications Use Case.
 */
public class TrackApplicationsController {

    private final TrackApplicationsInputBoundary trackApplicationsInteractor;

    /**
     * Creates the controller.
     * @param trackApplicationsInteractor the interactor that handles the request
     */
    public TrackApplicationsController(TrackApplicationsInputBoundary trackApplicationsInteractor) {
        this.trackApplicationsInteractor = trackApplicationsInteractor;
    }

    /**
     * Executes the Track Applications Use Case.
     * @param adopterId the adopter whose applications are shown
     */
    public void execute(String adopterId) {
        trackApplicationsInteractor.execute(new TrackApplicationsInputData(adopterId));
    }
}

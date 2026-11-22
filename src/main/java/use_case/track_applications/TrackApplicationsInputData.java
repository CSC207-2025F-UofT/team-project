package use_case.track_applications;

/**
 * The Input Data for the Track Applications Use Case.
 */
public final class TrackApplicationsInputData {

    private final String adopterId;

    /**
     * Creates the input data.
     * @param adopterId the adopter whose applications are shown
     */
    public TrackApplicationsInputData(String adopterId) {
        this.adopterId = adopterId;
    }

    String getAdopterId() {
        return adopterId;
    }
}

package interface_adapter.track_applications;

/**
 * Listens for changes to the Track Applications View Model.
 */
public interface TrackApplicationsViewModelListener {

    /**
     * The track applications state was updated.
     */
    void stateChanged();
}

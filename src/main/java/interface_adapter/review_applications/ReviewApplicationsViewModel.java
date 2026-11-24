package interface_adapter.review_applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.ApplicationStatus;

/**
 * The View Model for the Review Applications View.
 */
public final class ReviewApplicationsViewModel {

    public static final String VIEW_NAME = "review applications";

    /** The status filter option that shows every status. */
    public static final String ALL_STATUSES = "All";

    /** The choices for the status filter: {@link #ALL_STATUSES} first, then every status. */
    public static final List<String> STATUS_OPTIONS = statusOptions();

    private final List<ReviewApplicationsViewModelListener> listeners = new ArrayList<>();

    private ReviewApplicationsState state = new ReviewApplicationsState();

    public ReviewApplicationsState getState() {
        return this.state;
    }

    public void setState(ReviewApplicationsState state) {
        this.state = state;
    }

    /**
     * Registers a listener that is told when the state changes.
     * @param listener the listener to add
     */
    public void addListener(ReviewApplicationsViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state changed.
     */
    public void notifyStateChanged() {
        for (ReviewApplicationsViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    private static List<String> statusOptions() {
        final List<String> options = new ArrayList<>();
        options.add(ALL_STATUSES);
        for (ApplicationStatus status : ApplicationStatus.values()) {
            options.add(status.name());
        }
        return Collections.unmodifiableList(options);
    }
}

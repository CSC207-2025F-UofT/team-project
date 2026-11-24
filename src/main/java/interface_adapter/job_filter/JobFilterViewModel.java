package interface_adapter.job_filter;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Job Filter View.
 */
public class JobFilterViewModel {

    private final List<JobFilterViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "job filter";

    private JobFilterState state = new JobFilterState(List.of(), null);

    public JobFilterState getState() {
        return this.state;
    }

    public void setState(JobFilterState state) {
        this.state = state;
    }

    public void addListener(JobFilterViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (JobFilterViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

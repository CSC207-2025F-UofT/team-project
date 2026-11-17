package interface_adapter.job_list;

import java.util.ArrayList;
import java.util.List;

/**
 * The view model for the JobList view.
 */
public class JobListViewModel {

    public static final String TITLE_LABEL = "Job List";

    private final List<JobListViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "job list";

    private JobListState state = new JobListState();

    public JobListState getState() {
        return this.state;
    }

    public void setState(JobListState state) {
        this.state = state;
    }

    public void addListener(JobListViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (JobListViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

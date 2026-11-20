package interface_adapter.job_grouping;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Create Job Group View.
 */
public class CreateJobGroupViewModel {

    private final List<CreateJobGroupViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "create job group";

    private CreateJobGroupState state = new CreateJobGroupState();

    public CreateJobGroupState getState() {
        return this.state;
    }

    public void setState(CreateJobGroupState state) {
        this.state = state;
    }

    public void addListener(CreateJobGroupViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (CreateJobGroupViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

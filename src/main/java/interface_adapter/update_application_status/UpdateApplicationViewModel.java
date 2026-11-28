package interface_adapter.update_application_status;

import java.util.ArrayList;
import java.util.List;

public class UpdateApplicationViewModel {
    public static final String VIEW_NAME = "update application status";
    private UpdateApplicationState updateApplicationState = new UpdateApplicationState();

    private final List<UpdateApplicationViewModelListener> listeners = new ArrayList<>();

    public UpdateApplicationState getState() {return updateApplicationState;}

    public void setState(UpdateApplicationState updateApplicationState) {
        this.updateApplicationState = updateApplicationState;
    }

    public void addListener(UpdateApplicationViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (UpdateApplicationViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

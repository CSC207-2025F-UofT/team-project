package interface_adapter.save;

import java.util.ArrayList;
import java.util.List;

public class SaveViewModel {
    public static final String VIEW_NAME = "save";

    private final List<SaveViewModelListener> listeners = new ArrayList<>();
    private SaveState state = new SaveState();

    public SaveState getState() {
        return state;
    }

    public void setState(SaveState state) {
        this.state = state;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void addListener(SaveViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (SaveViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

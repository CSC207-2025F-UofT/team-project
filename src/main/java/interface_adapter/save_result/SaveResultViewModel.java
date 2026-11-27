package interface_adapter.save_result;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Holds what the save result screen should show. The view listens to it and redraws on change.
 */
public class SaveResultViewModel {
    public static final String VIEW_NAME = "save result";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String message = "";
    private String error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /** Tells every listening view that something changed. */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this);
    }

    // View listens to viewModel
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
package interface_adapter.continue_game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Holds the message shown on the title screen.
 */
public class TitleViewModel {
    public static final String VIEW_NAME = "title";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String message = "";

    public void setMessage(String message) {
        this.message = message;
        support.firePropertyChange("message", null, message);
    }

    public String getMessage() {
        return message;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}

package interface_adapter.logged_in;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The View Model for the Logged In View.
 */
public class LoggedInViewModel extends ViewModel<LoggedInState> {

    // 1. ADD PropertyChangeSupport FIELD
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public LoggedInViewModel() {
        super("logged in");
        setState(new LoggedInState());
    }

    // 2. ADD firePropertyChanged(String) METHOD
    /**
     * Fires a property change event with the given property name.
     * This is used for specific state updates (like "username" or "password").
     * @param propertyName The name of the property that changed.
     */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, this.getState());
    }

    // 3. OVERRIDE addPropertyChangeListener for views
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // Note: If your base ViewModel is supposed to handle these, you should move
    // the support field and these methods into your base ViewModel class instead.
}
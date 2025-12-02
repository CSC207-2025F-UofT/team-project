package interface_adapter;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel for our CA implementation.
 * This class delegates work to a PropertyChangeSupport object for
 * managing the property change events.
 *
 * @param <T> The type of state object contained in the model.
 */

public class ViewModel<T> {
    private final String viewName;

    private T state;
    
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public void setState(T state) {
        this.state = state;
    }

    public String getViewName() {
        return viewName;
    }

    public T getState() {
        return this.state;
    }

    /**
     * Fires a property change event for the "state" property.
     * Typically used when the entire state object has been updated.
     */
    public void firePropertyChange() {
        this.support.firePropertyChange("state", null, this.state);
    }

    /**
     * Fires a property change event for a specific property name,
     * using the current state as the new value.
     *
     * @param propertyName the name of the changed property
     */
    public void firePropertyChange(String propertyName) {
        this.support.firePropertyChange(propertyName, null, this.state);
    }

    /**
     * Fires a property change event with a specified new value.
     * Useful for event notifications such as login, logout, navigation, etc.
     *
     * @param propertyName the name of the event or property
     * @param oldValue     the old value associated with the event
     * @param newValue     the new value associated with the event
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.support.firePropertyChange(propertyName, oldValue, newValue);
    }
    /**
     * Adds a listener that will be notified when any property change event occurs.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

}

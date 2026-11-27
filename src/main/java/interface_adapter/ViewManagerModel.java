package interface_adapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of which screen should be showing, by name.
 * When the name changes, every listener (the ViewManager) is told about it.
 */

public class ViewManagerModel {
    private final List<ViewManagerModelListener> listeners = new ArrayList<>();
    private String currentViewName = "";

    /**
     * Returns the name of the screen that should be showing.
     *
     * @return the current view name
     */
    public String getCurrentViewName() {
        return currentViewName;
    }

    /**
     * Sets which screen should be showing. Call notifyViewChanged() afterwards.
     *
     * @param currentViewName the name of the screen to show
     */
    public void setState(String currentViewName) {
        this.currentViewName = currentViewName;
    }

    /**
     * Registers a listener to be told when the screen changes.
     *
     * @param listener the listener to add
     */
    public void addListener(ViewManagerModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the screen has changed.
     */
    public void notifyViewChanged() {
        for (ViewManagerModelListener listener : listeners) {
            listener.viewChanged();
        }
    }
}

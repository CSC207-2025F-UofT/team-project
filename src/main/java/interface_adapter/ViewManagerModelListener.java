package interface_adapter;

/**
 * Something that wants to know when the current screen changes.
 */
public interface ViewManagerModelListener {

    /**
     * Called when the ViewManagerModel's current view name changes.
     */
    void viewChanged();
}

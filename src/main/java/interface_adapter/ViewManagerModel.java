package interface_adapter;

/**
 * Keeps track of which view is currently shown. The state is the active view's name.
 */
public class ViewManagerModel extends ViewModel<String> {

    public ViewManagerModel() {
        super("view manager");
        setState("");
    }

    /** Switches the window to the view with the given name. */
    public void switchTo(String viewName) {
        setState(viewName);
        firePropertyChanged();
    }
}

package interface_adapter;

/**
 * Switches the window to another screen, for buttons like Back that only navigate.
 */
public class SwitchViewController {
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates the controller.
     *
     * @param viewManagerModel the model that chooses which screen shows
     */
    public SwitchViewController(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Shows another screen.
     *
     * @param viewName the name the screen was registered under
     */
    public void switchTo(String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.notifyViewChanged();
    }
}

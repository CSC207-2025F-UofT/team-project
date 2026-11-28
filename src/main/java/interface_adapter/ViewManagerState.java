package interface_adapter;

/**
 * Which screen is showing.
 */
public class ViewManagerState {
    private String activeView;

    /**
     * Returns the name of the screen that should be showing.
     *
     * @return the active view name, or null if none was chosen yet
     */
    public String getActiveView() {
        return activeView;
    }

    /**
     * Chooses the screen to show.
     *
     * @param activeView the name the screen was registered under
     */
    public void setActiveView(String activeView) {
        this.activeView = activeView;
    }
}

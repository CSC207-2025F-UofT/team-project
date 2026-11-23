package interface_adapters.menu_bar;

/**
 * The State information representing the Navigation Bar.
 */
public class NavigationBarState {
    private String currentViewName = "";
    private boolean isTimerRunning = false;

    /**
     * Returns the name of the current view being displayed.
     */
    public String getCurrentViewName() {
        return currentViewName;
    }

    /**
     * Sets the name of the current view.
     */
    public void setCurrentViewName(String currentViewName) {
        this.currentViewName = currentViewName;
    }

    /**
     * Returns whether the current timer is running.
     */
    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    /**
     * Sets whether the timer is currently running.
     */
    public void setTimerRunning(boolean timerRunning) {
        isTimerRunning = timerRunning;
    }
}

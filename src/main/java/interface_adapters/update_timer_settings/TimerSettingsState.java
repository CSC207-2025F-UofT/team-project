package interface_adapters.update_timer_settings;

/**
 * The State information representing the Timer Setting.
 */
public class TimerSettingsState {
    private String errorMessage = null;
    private boolean settingsOpen = false;

    /**
     * Returns the error message.
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Sets the error message in the view state.
     */
    public void setErrorMessage(String error) {
        this.errorMessage = error;
    }

    /**
     * Returns if the Timer Settings View is open.
     */
    public boolean getSettingsOpen() { return settingsOpen; }

    /**
     * Sets the flag to whether the Timer Settings View is open.
     */
    public void setSettingsOpen(boolean settingsOpen) {
        this.settingsOpen = settingsOpen;
    }
}

package interface_adapters.home;

/**
 * The State information representing the Home Page.
 */
public class HomeState {
    private String timerDisplay = "00:00";
    private int totalSeconds = 0;
    private boolean timerRunning = false;

    /**
     * Returns the timer's current display value.
     * @return the remaining time formatted as a string
     */
    public String getTimerDisplay() { return timerDisplay; }

    /**
     * Sets the timer's display value.
     * @param timerDisplay the remaining time formatted as a string
     */
    public void setTimerDisplay(String timerDisplay) {this.timerDisplay = timerDisplay; }

    /**
     * Sets the total remaining time.
     * @param totalSeconds the remaining time in seconds
     */
    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    /**
     * Returns whether the timer is currently running.
     * @return true if the timer is running, false otherwise
     */
    public boolean isTimerRunning() {
        return timerRunning;
    }

    /**
     * Updates the timer's running state.
     * @param timerRunning true if the timer is running, false otherwise
     */
    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }
}

package use_case.cancel_timer;

/**
 * The DAO interface for the Cancel Timer Use Case.
 */
public interface CancelTimerDataAccessInterface {
    /**
     * Sets whether the timer is currently running.
     * @param isTimerRunning true if the timer is running, false otherwise
     */
    void setTimerRunning (boolean isTimerRunning);
}

package use_case.start_timer;

/**
 * The DAO interface for the Start Timer Use Case.
 */
public interface StartTimerDataAccessInterface {
    /**
     * Returns the duration of the break session in minutes.
     */
    int getBreakDuration();

    /**
     * Returns the duration of the focus session in minutes.
     */
    int getFocusDuration();

    /**
     * Returns whether the timer is currently running.
     */
    boolean isTimerRunning();

    /**
     * Updates the timer's running state.
     */
    void setTimerRunning(boolean isTimerRunning);
}

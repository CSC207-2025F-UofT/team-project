package use_case.start_timer;

/**
 * The output boundary for the Start Timer Use Case.
 */
public interface StartTimerOutputBoundary {
    void prepareSuccessView(StartTimerOutputData outputData);

    /**
     * Updates the timer display during the countdown.
     * @param timerString the remaining time formatted as a string
     */
    void updateTimerCountdown(String timerString);

    /**
     * Prepares the Start Timer View Model when the timer finishes running.
     */
    void prepareTimerFinishedView();
}

package entities;

/**
 * Factory for creating Session objects.
 */
public class TimerSessionFactory {
    private static final int DEFAULT_FOCUS = 25;
    private static final int DEFAULT_BREAK = 5;

    /**
     * Creates a custom session using the durations provided.
     * @param focusDuration the focus session duration in minutes
     * @param breakDuration the break session duration in minutes
     * @return a new Session with the specified focus and break durations
     */
    public Session create (int focusDuration, int breakDuration) {
        return new Session(focusDuration, breakDuration);
    }

    /**
     * Creates a session using the default focus and break durations.
     * @return a new Session with the default focus and break durations
     */
    public Session createDefault() {
        return new Session(DEFAULT_FOCUS, DEFAULT_BREAK);
    }
}

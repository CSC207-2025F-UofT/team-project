package use_case.start_timer;

/**
 * Output Data for the Start Timer Use Case.
 */
public class StartTimerOutputData {
    private final int durationMinutes;
    private final String sessionType;

    public StartTimerOutputData(int durationMinutes, String sessionType) {
        this.durationMinutes = durationMinutes;
        this.sessionType = sessionType;
    }

    /**
     * Returns the type of the session.
     */
    public String getSessionType() {
        return sessionType;
    }

    /**
     * Returns the duration of the session in minutes.
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }
}

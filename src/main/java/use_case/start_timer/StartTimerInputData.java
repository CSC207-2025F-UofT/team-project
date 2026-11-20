package use_case.start_timer;

/**
 * The input data for the Start Timer Use Case.
 */
public class StartTimerInputData {
    private final String sessionType;

    public StartTimerInputData(String sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * Returns the type of session to start.
     */
    public String getSessionType() {
        return sessionType;
    }
}

package use_case.save_session;

/**
 * The input data for the Save Session Use Case.
 */
public class SaveSessionInputData {
    private int focusDuration;
    private int breakDuration;

    public SaveSessionInputData(int focusDuration, int breakDuration) {
        this.focusDuration = focusDuration;
        this.breakDuration = breakDuration;
    }

    /**
     * Returns the length of the focus session in minutes.
     */
    public int getFocusDuration() {
        return focusDuration;
    }

    /**
     * Returns the length of the break session in minutes.
     */
    public int getBreakDuration() {
        return breakDuration;
    }
}

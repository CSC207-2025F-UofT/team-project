package use_case.save_session;

public class SaveSessionOutputData {
    private int focusDuration;
    private int breakDuration;

    /**
     * Output Data for the Save Session Use Case.
     */
    public SaveSessionOutputData(int focusDuration, int breakDuration) {
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

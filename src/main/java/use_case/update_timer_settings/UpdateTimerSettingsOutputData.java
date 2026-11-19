package use_case.update_timer_settings;

public class UpdateTimerSettingsOutputData {
    private int focusDuration;
    private int breakDuration;

    /**
     * Output Data for the Update Timer Settings Use Case.
     */
    public UpdateTimerSettingsOutputData(int focusDuration, int breakDuration) {
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

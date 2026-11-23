package entities;

/**
 * A simple entity representing a study sessions. A session has a focus timer and a break timer.
 */
public class Session {
    private int focusDuration;
    private int breakDuration;

    public Session(int focusDuration, int breakDuration) {
        this.focusDuration = focusDuration;
        this.breakDuration = breakDuration;
    }

    public int getFocusDuration() {
        return this.focusDuration;
    }

    public int getBreakDuration() { return this.breakDuration; }

}
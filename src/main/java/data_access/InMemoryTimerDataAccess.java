package data_access;

import entities.Session;
import entities.TimerSessionFactory;
import use_case.cancel_timer.CancelTimerDataAccessInterface;
import use_case.start_timer.StartTimerDataAccessInterface;
import use_case.update_timer_settings.UpdateTimerSettingsDataAccessInterface;

/**
 * In-memory implementation of the DAO for storing length of focus and break sessions. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryTimerDataAccess implements UpdateTimerSettingsDataAccessInterface,
        StartTimerDataAccessInterface, CancelTimerDataAccessInterface {
    private Session session;
    private boolean isTimerRunning;

    public InMemoryTimerDataAccess(TimerSessionFactory factory) {
        // Set the default values the moment the app opens. The factory stores default timer values
        this.session = factory.createDefault();
    }

    /**
     * Saves the provided study session by replacing the currently stored session.
     * @param session the study session to store
     */
    @Override
    public void save(Session session) {
        this.session = session;
    }

    /**
     * Returns the duration of the focus session.
     * @return the focus duration in minutes
     */
    public int getFocusDuration() {
        return session.getFocusDuration();
    }

    /**
     * Returns the duration of the break session.
     * @return the focus duration in minutes
     */
    public int getBreakDuration() {
        return session.getBreakDuration();
    }

    /**
     * Updates the timer's running state.
     * @param timerRunning true if the timer is running, false otherwise
     */
    @Override
    public void setTimerRunning(boolean timerRunning) {
        isTimerRunning = timerRunning;
    }

    /**
     * Returns the duration of the break session.
     * @return true if the timer is running, false otherwise
     */
    @Override
    public boolean isTimerRunning() {
        return isTimerRunning;
    }
}

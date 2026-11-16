package data_access;

import entities.Session;
import entities.TimerSessionFactory;
import use_case.save_session.SaveSessionDataAccessInterface;

/**
 * In-memory implementation of the DAO for storing focus and break sessions. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemorySaveSessionDataAccess implements SaveSessionDataAccessInterface {
    private Session session;

    public InMemorySaveSessionDataAccess(TimerSessionFactory factory) {
        // Set the default values the moment the app opens. The factory stores default timer values
        this.session = factory.createDefault();
    }

    /**
     * Saves the provided study session.
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
}

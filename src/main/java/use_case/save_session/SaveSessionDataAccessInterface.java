package use_case.save_session;

import entities.Session;

/**
 * The DAO interface for the Save Session Use Case.
 */
public interface SaveSessionDataAccessInterface {

    /**
     * Saves the session.
     * @param session a study session
     */
    void save(Session session);
}

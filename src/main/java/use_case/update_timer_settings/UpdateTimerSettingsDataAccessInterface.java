package use_case.update_timer_settings;

import entities.Session;

/**
 * The DAO interface for the Update Timer Settings Use Case.
 */
public interface UpdateTimerSettingsDataAccessInterface {

    /**
     * Saves the session.
     * @param session a study session
     */
    void save(Session session);
}

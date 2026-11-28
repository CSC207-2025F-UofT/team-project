package use_case.save_preferences;

import entity.AdopterPreferences;

/**
 * Receives the result of the save preferences use case.
 */
public interface SavePreferencesOutputBoundary {

    /**
     * Tells the adopter the answers were saved.
     *
     * @param preferences what was saved
     */
    void presentSaved(AdopterPreferences preferences);

    /**
     * Shows what the adopter saved before.
     *
     * @param preferences what was saved, or null if nothing was saved yet
     */
    void presentLoaded(AdopterPreferences preferences);

    /**
     * Tells the adopter the answers could not be saved.
     *
     * @param message what went wrong
     */
    void presentError(String message);
}

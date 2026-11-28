package use_case.save_preferences;

/**
 * Starts the save preferences use case.
 */
public interface SavePreferencesInputBoundary {

    /**
     * Saves the adopter's answers, replacing any saved before.
     *
     * @param inputData what the adopter ticked
     */
    void execute(SavePreferencesInputData inputData);

    /**
     * Shows what the adopter saved before, so the page can start filled in.
     *
     * @param adopterId the adopter
     */
    void load(String adopterId);
}

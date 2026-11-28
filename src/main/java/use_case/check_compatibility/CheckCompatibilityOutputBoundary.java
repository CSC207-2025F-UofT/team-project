package use_case.check_compatibility;

/**
 * Receives the result of the check pet compatibility use case.
 */
public interface CheckCompatibilityOutputBoundary {

    /**
     * Shows how the cat fits the adopter.
     *
     * @param outputData the cat's name and one result per rule
     */
    void presentCompatibility(CheckCompatibilityOutputData outputData);

    /**
     * Tells the adopter to fill in their preferences first.
     */
    void presentNoPreferences();

    /**
     * Tells the adopter the cat could not be found.
     *
     * @param message what to show
     */
    void presentNotFound(String message);
}

package use_case.check_compatibility;

/**
 * Starts the check pet compatibility use case.
 */
public interface CheckCompatibilityInputBoundary {

    /**
     * Checks one cat against the adopter's saved preferences.
     *
     * @param inputData the cat and the adopter
     */
    void execute(CheckCompatibilityInputData inputData);
}

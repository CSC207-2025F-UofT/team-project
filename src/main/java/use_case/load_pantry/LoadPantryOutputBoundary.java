package use_case.load_pantry;

/**
 * Output boundary for loading the pantry.
 */
public interface LoadPantryOutputBoundary {

    /**
     * Presents the loaded pantry.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(LoadPantryOutputData outputData);
}

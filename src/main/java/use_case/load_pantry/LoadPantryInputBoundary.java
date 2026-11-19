package use_case.load_pantry;

/**
 * Input boundary for loading the pantry.
 */
public interface LoadPantryInputBoundary {

    /**
     * Loads the current pantry.
     *
     * @param inputData the input data
     */
    void execute(LoadPantryInputData inputData);
}

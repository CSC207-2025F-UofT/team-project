package use_case.load_map;

/** Accepts requests to load a complete terrain map. */
public interface LoadMapInputBoundary {
    /**
     * Loads the requested map and reports exactly one success or failure through the output boundary.
     * Invalid names and loading failures are presented rather than thrown to the caller.
     *
     * @param inputData non-null request containing the map name
     */
    void execute(LoadMapInputData inputData);
}

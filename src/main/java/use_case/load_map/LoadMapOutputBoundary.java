package use_case.load_map;

/** Receives either a complete map snapshot or an explanation of a failed load. */
public interface LoadMapOutputBoundary {
    /**
     * Presents the complete map and clears any earlier loading error.
     *
     * @param outputData non-null, immutable snapshot of a successfully loaded map
     */
    void prepareSuccessView(LoadMapOutputData outputData);

    /**
     * Presents a loading error without replacing any previously displayed map.
     *
     * @param error non-blank explanation of why the requested map could not be loaded
     */
    void prepareFailView(String error);
}

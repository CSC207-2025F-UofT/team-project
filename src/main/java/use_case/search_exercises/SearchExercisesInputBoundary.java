package use_case.search_exercises;

/**
 * The entry point of the Search Exercises use case. The controller calls it.
 */
public interface SearchExercisesInputBoundary {

    /**
     * Searches the exercise library and reports the result to the output boundary.
     *
     * @param inputData what the user asked to search for
     */
    void execute(SearchExercisesInputData inputData);
}

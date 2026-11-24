package use_case.search_exercises;

/**
 * How the Search Exercises use case reports its result. The presenter implements it.
 */
public interface SearchExercisesOutputBoundary {

    /**
     * Reports a finished search. An empty result list means nothing matched; that is not an error.
     *
     * @param outputData the exercises found
     */
    void prepareSuccessView(SearchExercisesOutputData outputData);

    /**
     * Reports that the search could not be done.
     *
     * @param errorMessage a message to show the user
     */
    void prepareFailView(String errorMessage);
}

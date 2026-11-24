package use_case.search_exercises;

import java.util.List;

import entity.Exercise;

/**
 * Searches the exercise library for what the user asked, and reports the matches or the reason
 * there are none.
 */
public class SearchExercisesInteractor implements SearchExercisesInputBoundary {
    static final String EMPTY_QUERY_MESSAGE = "Type a name, or choose a muscle or equipment.";
    static final String UNAVAILABLE_MESSAGE = "Exercise search is unavailable right now.";

    private final ExerciseSearchDataAccessInterface exerciseSearch;
    private final SearchExercisesOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param exerciseSearch where to search; any implementation works (web API, cache or fake)
     * @param presenter      where to report the result
     */
    public SearchExercisesInteractor(ExerciseSearchDataAccessInterface exerciseSearch,
                                     SearchExercisesOutputBoundary presenter) {
        this.exerciseSearch = exerciseSearch;
        this.presenter = presenter;
    }

    /**
     * Searches the library. An empty query is refused without searching, so it costs no request.
     * If the library can't be reached, the user gets a short message instead of an error.
     *
     * @param inputData what the user asked to search for
     */
    @Override
    public void execute(SearchExercisesInputData inputData) {
        ExerciseSearchQuery query = new ExerciseSearchQuery(
                inputData.name(), inputData.bodyPart(), inputData.equipment());
        if (query.isEmpty()) {
            presenter.prepareFailView(EMPTY_QUERY_MESSAGE);
            return;
        }
        try {
            List<Exercise> matches = exerciseSearch.search(query);
            presenter.prepareSuccessView(new SearchExercisesOutputData(toResults(matches)));
        } catch (ExerciseLibraryUnavailableException e) {
            presenter.prepareFailView(UNAVAILABLE_MESSAGE);
        }
    }

    private static List<SearchExercisesOutputData.ExerciseResult> toResults(List<Exercise> exercises) {
        return exercises.stream().map(SearchExercisesInteractor::toResult).toList();
    }

    private static SearchExercisesOutputData.ExerciseResult toResult(Exercise exercise) {
        return new SearchExercisesOutputData.ExerciseResult(
                exercise.getExerciseID(),
                exercise.getName(),
                exercise.getBodyPart().name(),
                exercise.getTarget(),
                exercise.getSecondaryMuscles(),
                exercise.getEquipment(),
                exercise.getMechanic(),
                exercise.getInstructions());
    }
}

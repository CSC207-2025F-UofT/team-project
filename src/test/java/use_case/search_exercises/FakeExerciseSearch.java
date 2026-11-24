package use_case.search_exercises;

import java.util.ArrayList;
import java.util.List;

import entity.Exercise;

/**
 * A fake exercise library for tests: it never touches the network. It either returns the same
 * exercises for every query, or fails every time, and it records each query it was asked.
 */
public class FakeExerciseSearch implements ExerciseSearchDataAccessInterface {
    private final List<Exercise> exercisesToReturn;
    private final boolean unavailable;
    private final List<ExerciseSearchQuery> receivedQueries = new ArrayList<>();

    private FakeExerciseSearch(List<Exercise> exercisesToReturn, boolean unavailable) {
        this.exercisesToReturn = exercisesToReturn;
        this.unavailable = unavailable;
    }

    /**
     * Creates a fake that answers every query with the given exercises.
     *
     * @param exercises what every search returns
     * @return the fake
     */
    public static FakeExerciseSearch returning(List<Exercise> exercises) {
        return new FakeExerciseSearch(List.copyOf(exercises), false);
    }

    /**
     * Creates a fake whose every search fails, as if the library were unreachable.
     *
     * @return the fake
     */
    public static FakeExerciseSearch unavailable() {
        return new FakeExerciseSearch(List.of(), true);
    }

    @Override
    public List<Exercise> search(ExerciseSearchQuery query) throws ExerciseLibraryUnavailableException {
        receivedQueries.add(query);
        if (unavailable) {
            throw new ExerciseLibraryUnavailableException("fake library is unavailable", null);
        }
        return exercisesToReturn;
    }

    /** Returns every query received, in order. */
    public List<ExerciseSearchQuery> getReceivedQueries() {
        return receivedQueries;
    }
}

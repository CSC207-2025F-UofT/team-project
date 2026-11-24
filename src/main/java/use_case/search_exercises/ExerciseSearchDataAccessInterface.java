package use_case.search_exercises;

import java.util.List;

import entity.Exercise;

/**
 * Searches the exercise library. The use case depends on this interface, and outer layers supply
 * the implementation: the real web API, a cache in front of it, or a fake for tests.
 */
public interface ExerciseSearchDataAccessInterface {

    /**
     * Finds the exercises that match every part of the query.
     *
     * @param query what to search for
     * @return the matching exercises; empty if nothing matches, never {@code null}
     * @throws ExerciseLibraryUnavailableException if the library can't be reached
     */
    List<Exercise> search(ExerciseSearchQuery query) throws ExerciseLibraryUnavailableException;
}

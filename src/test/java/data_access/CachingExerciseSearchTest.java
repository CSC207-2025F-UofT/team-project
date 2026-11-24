package data_access;

import java.util.List;

import entity.BodyPart;
import entity.Exercise;
import org.junit.jupiter.api.Test;
import use_case.search_exercises.ExerciseLibraryUnavailableException;
import use_case.search_exercises.ExerciseSearchQuery;
import use_case.search_exercises.FakeExerciseSearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CachingExerciseSearchTest {
    private static final Exercise BENCH_PRESS = new Exercise("0025", "Barbell Bench Press", "A chest press.",
            BodyPart.CHEST, "Pectorals", List.of("Triceps"), "Barbell", "compound", List.of("Press the bar up."));

    @Test
    void repeatedSearchIsAnsweredFromTheCache() throws ExerciseLibraryUnavailableException {
        FakeExerciseSearch library = FakeExerciseSearch.returning(List.of(BENCH_PRESS));
        CachingExerciseSearch cache = new CachingExerciseSearch(library);

        cache.search(new ExerciseSearchQuery("Bench", BodyPart.CHEST, null));
        List<Exercise> second = cache.search(new ExerciseSearchQuery("  bench ", BodyPart.CHEST, null));

        assertEquals(1, library.getReceivedQueries().size());
        assertEquals(List.of(BENCH_PRESS), second);
    }

    @Test
    void differentSearchesAreEachSentOnce() throws ExerciseLibraryUnavailableException {
        FakeExerciseSearch library = FakeExerciseSearch.returning(List.of(BENCH_PRESS));
        CachingExerciseSearch cache = new CachingExerciseSearch(library);

        cache.search(new ExerciseSearchQuery("bench", null, null));
        cache.search(new ExerciseSearchQuery("bench", BodyPart.CHEST, null));

        assertEquals(2, library.getReceivedQueries().size());
    }

    @Test
    void failuresAreNotCached() {
        FakeExerciseSearch library = FakeExerciseSearch.unavailable();
        CachingExerciseSearch cache = new CachingExerciseSearch(library);
        ExerciseSearchQuery query = new ExerciseSearchQuery("bench", null, null);

        assertThrows(ExerciseLibraryUnavailableException.class, () -> cache.search(query));
        assertThrows(ExerciseLibraryUnavailableException.class, () -> cache.search(query));

        assertEquals(2, library.getReceivedQueries().size());
    }
}

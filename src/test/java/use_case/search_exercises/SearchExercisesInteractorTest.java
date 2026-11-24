package use_case.search_exercises;

import java.util.List;

import entity.BodyPart;
import entity.Exercise;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchExercisesInteractorTest {
    private static final Exercise BENCH_PRESS = new Exercise("0025", "Barbell Bench Press", "A chest press.",
            BodyPart.CHEST, "Pectorals", List.of("Triceps", "Shoulders"), "Barbell", "compound",
            List.of("Lie on the bench.", "Press the bar up."));

    /** Records what the interactor reported, so the test can check it. */
    private static final class RecordingPresenter implements SearchExercisesOutputBoundary {
        private SearchExercisesOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(SearchExercisesOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String message) {
            errorMessage = message;
        }
    }

    private final RecordingPresenter presenter = new RecordingPresenter();

    @Test
    void reportsEachMatchWithTheDetailsTheScreenShows() {
        FakeExerciseSearch library = FakeExerciseSearch.returning(List.of(BENCH_PRESS));

        new SearchExercisesInteractor(library, presenter)
                .execute(new SearchExercisesInputData("press", BodyPart.CHEST, "barbell"));

        SearchExercisesOutputData.ExerciseResult result = presenter.successData.results().get(0);
        assertEquals("0025", result.exerciseID());
        assertEquals("Barbell Bench Press", result.name());
        assertEquals("CHEST", result.bodyPart());
        assertEquals(List.of("Triceps", "Shoulders"), result.secondaryMuscles());
        assertEquals("compound", result.mechanic());
        assertEquals(new ExerciseSearchQuery("press", BodyPart.CHEST, "barbell"), library.getReceivedQueries().get(0));
    }

    @Test
    void noMatchesIsASuccessWithNoResults() {
        new SearchExercisesInteractor(FakeExerciseSearch.returning(List.of()), presenter)
                .execute(new SearchExercisesInputData("zzz", null, null));

        assertTrue(presenter.successData.results().isEmpty());
        assertNull(presenter.errorMessage);
    }

    @Test
    void emptyQueryIsRefusedWithoutSearching() {
        FakeExerciseSearch library = FakeExerciseSearch.returning(List.of(BENCH_PRESS));

        new SearchExercisesInteractor(library, presenter).execute(new SearchExercisesInputData("   ", null, null));

        assertEquals(SearchExercisesInteractor.EMPTY_QUERY_MESSAGE, presenter.errorMessage);
        assertTrue(library.getReceivedQueries().isEmpty());
    }

    @Test
    void unreachableLibraryGivesAFriendlyMessage() {
        new SearchExercisesInteractor(FakeExerciseSearch.unavailable(), presenter)
                .execute(new SearchExercisesInputData("press", null, null));

        assertEquals(SearchExercisesInteractor.UNAVAILABLE_MESSAGE, presenter.errorMessage);
        assertNull(presenter.successData);
    }
}

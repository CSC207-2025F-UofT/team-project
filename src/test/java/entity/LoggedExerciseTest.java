package entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoggedExerciseTest {
    private static final Exercise BENCH_PRESS = new Exercise("0025", "Barbell Bench Press", "A chest press.",
            BodyPart.CHEST, "Pectorals", List.of(), "Barbell", "compound", List.of());

    @Test
    void reusingTheCallersListDoesNotChangeWhatWasLogged() {
        List<ExerciseSet> currentSets = new ArrayList<>(List.of(new ExerciseSet(80, 5), new ExerciseSet(80, 5)));
        LoggedExercise bench = new LoggedExercise(BENCH_PRESS, currentSets);

        currentSets.clear();
        currentSets.add(new ExerciseSet(100, 5));

        assertEquals(2, bench.getCompletedSets().size());
        assertEquals(80, bench.getCompletedSets().get(0).getWeight());
    }

    @Test
    void completedSetsCannotBeChangedFromOutside() {
        LoggedExercise bench = new LoggedExercise(BENCH_PRESS, List.of(new ExerciseSet(80, 5)));

        assertThrows(UnsupportedOperationException.class, () -> bench.getCompletedSets().clear());
    }
}

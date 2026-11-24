package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkoutAndPlanListsTest {
    private static final Exercise BENCH_PRESS = new Exercise("0025", "Barbell Bench Press", "A chest press.",
            BodyPart.CHEST, "Pectorals", List.of(), "Barbell", "compound", List.of());
    private static final UUID OWNER = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void workoutKeepsItsOwnCopyOfTheExercises() {
        List<LoggedExercise> exercises = new ArrayList<>(List.of(new LoggedExercise(BENCH_PRESS, List.of())));
        Workout workout = new Workout(UUID.randomUUID(), OWNER, UUID.randomUUID(), "Push Day",
                LocalDate.of(2026, 11, 23), exercises);

        exercises.clear();

        assertEquals(1, workout.getExercises().size());
        assertThrows(UnsupportedOperationException.class, () -> workout.getExercises().clear());
    }

    @Test
    void planKeepsItsOwnCopyOfTheExercises() {
        List<PlannedExercise> exercises = new ArrayList<>(List.of(new PlannedExercise(BENCH_PRESS, 3, 8, 80.0)));
        WorkoutPlan plan = new WorkoutPlan(UUID.randomUUID(), "Push Day", OWNER, exercises, false, false, null);

        exercises.clear();

        assertEquals(1, plan.getExercises().size());
        assertThrows(UnsupportedOperationException.class, () -> plan.getExercises().clear());
    }
}

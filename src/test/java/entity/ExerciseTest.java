package entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExerciseTest {
    private static Exercise exerciseNamed(String name, List<String> secondaryMuscles) {
        return new Exercise("0025", name, "A chest exercise.", BodyPart.CHEST, "Pectorals",
                secondaryMuscles, "Barbell", "compound", List.of("Lie on the bench.", "Press the bar up."));
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> exerciseNamed("  ", List.of()));
    }

    @Test
    void keepsItsOwnCopyOfTheSecondaryMuscles() {
        List<String> callersList = new ArrayList<>(List.of("Triceps"));
        Exercise exercise = exerciseNamed("Barbell Bench Press", callersList);

        callersList.add("Shoulders");

        assertEquals(List.of("Triceps"), exercise.getSecondaryMuscles());
    }

    @Test
    void secondaryMusclesCannotBeChangedFromOutside() {
        Exercise exercise = exerciseNamed("Barbell Bench Press", List.of("Triceps"));

        assertThrows(UnsupportedOperationException.class, () -> exercise.getSecondaryMuscles().add("Shoulders"));
    }
}

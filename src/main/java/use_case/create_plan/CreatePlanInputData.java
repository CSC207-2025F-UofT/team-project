package use_case.create_plan;

import java.util.List;
import java.util.UUID;

public record CreatePlanInputData(UUID ownerId, String name, List<ExerciseEntry> exercises) {
    public record ExerciseEntry(String exerciseId, int sets, int reps, Double weight) {
    }
}

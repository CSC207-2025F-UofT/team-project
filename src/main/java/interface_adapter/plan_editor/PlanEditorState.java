package interface_adapter.plan_editor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlanEditorState {
    private UUID planId;
    private String name = "";
    private List<ExerciseRow> exercises = new ArrayList<>();
    private String error;

    public record ExerciseRow(String exerciseId, String exerciseName, int sets, int reps, Double weight) {
    }

    public UUID getPlanId() {
        return planId;
    }

    public String getName() {
        return name;
    }

    public List<ExerciseRow> getExercises() {
        return exercises;
    }

    public String getError() {
        return error;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setExercises(List<ExerciseRow> exercises) {
        this.exercises = exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNewPlan() {
        return planId == null;
    }
}

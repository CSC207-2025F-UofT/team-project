package use_case.create_plan;

import entity.Exercise;
import entity.PlannedExercise;
import entity.WorkoutPlan;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreatePlanInteractor implements CreatePlanInputBoundary {
    private static final int MAX_NAME_LENGTH = 50;
    private final CreatePlanDataAccessInterface planDataAccess;
    private final ExerciseLookupDataAccessInterface exerciseLookup;
    private final CreatePlanOutputBoundary presenter;

    public CreatePlanInteractor(CreatePlanDataAccessInterface planDataAccess,
                                ExerciseLookupDataAccessInterface exerciseLookup,
                                CreatePlanOutputBoundary presenter) {
        this.planDataAccess = planDataAccess;
        this.exerciseLookup = exerciseLookup;
        this.presenter = presenter;
    }

    @Override
    public void execute(CreatePlanInputData inputData) {
        UUID ownerId = inputData.ownerId();
        String name = inputData.name();
        List<CreatePlanInputData.ExerciseEntry> exercises = inputData.exercises();

        String inputCheck = inputChecker(ownerId, name, exercises);
        if (!inputCheck.isBlank()) {
            presenter.prepareFailView(inputCheck);
            return;
        }

        if (planDataAccess.existsByName(ownerId, name)) {
            presenter.prepareFailView("You already have a plan called \"" + name + "\".");
            return;
        }

        ArrayList<PlannedExercise> templateExercises = new ArrayList<>();
        int row = 0;
        for (CreatePlanInputData.ExerciseEntry entry : exercises) {
            row += 1;
            Exercise exercise = exerciseLookup.findById(entry.exerciseId());
            if (exercise == null) {
                presenter.prepareFailView("Row " + row + ": exercise not found.");
                return;
            }
            templateExercises.add(new PlannedExercise(exercise, entry.sets(), entry.reps(), entry.weight()));
        }

        WorkoutPlan newPlan = new WorkoutPlan(UUID.randomUUID(), name, ownerId,
                templateExercises, false, false, null);

        planDataAccess.save(newPlan);

        CreatePlanOutputData outputData = new CreatePlanOutputData(newPlan.getWorkoutPlanID(), newPlan.getName(),
                newPlan.getExercises().size());
        presenter.prepareSuccessView(outputData);
    }

    /**
     Helper method to check whether the entered input is correct. Returns empty string when correct.
     */
    private String inputChecker(UUID ownerId, String name, List<CreatePlanInputData.ExerciseEntry> exercises) {
        if (ownerId == null) {
            return "No user is logged in.";
        }
        else if (name == null || name.isBlank()) {
            return "Plan name is required.";
        }
        else if (name.length() > MAX_NAME_LENGTH) {
            return "Plan name must be at most " + MAX_NAME_LENGTH + " characters.";
        }
        else if (exercises == null || exercises.isEmpty()) {
            return "Add at least one exercise.";
        }

        int row = 0;

        for (CreatePlanInputData.ExerciseEntry exercise : exercises) {
            row += 1;
            if (exercise.exerciseId() == null || exercise.exerciseId().isBlank()) {
                return "Row " + row + ": choose an exercise.";
            }
            else if (exercise.sets() < 1) {
                return "Row " + row + ": must contain at least 1 set.";
            }
            else if (exercise.reps() < 1) {
                return "Row " + row + ": must contain at least 1 rep.";
            }
            else if (exercise.weight() != null && exercise.weight() < 0) {
                return "Row " + row + ": weights cannot be negative.";
            }
        }
        return "";
    }
}

package interface_adapter.create_plan;

import use_case.create_plan.CreatePlanInputBoundary;
import use_case.create_plan.CreatePlanInputData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreatePlanController {
    private final CreatePlanInputBoundary interactor;

    public CreatePlanController(CreatePlanInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(UUID ownerId, String name, List<String> exerciseIds, List<Integer> sets, List<Integer> reps,
                        List<Double> weights) {
        ArrayList<CreatePlanInputData.ExerciseEntry> exerciseEntries = new ArrayList<>();
        String trimmedName = null;
        int listSize = 0;

        if (name != null) {
            trimmedName = name.trim();
        }

        if (exerciseIds != null) {
            listSize = exerciseIds.size();
        }

        for (int i = 0; i < listSize; i++) {
            exerciseEntries.add(new CreatePlanInputData.ExerciseEntry(exerciseIds.get(i), sets.get(i), reps.get(i),
                    weights.get(i)));
        }

        CreatePlanInputData inputData = new CreatePlanInputData(ownerId, trimmedName, exerciseEntries);

        interactor.execute(inputData);
    }
}

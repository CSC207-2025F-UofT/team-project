package interface_adapter.meal_planning;

import use_case.meal_planning.CreatePlanInputBoundary;
import use_case.meal_planning.CreatePlanInputData;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CreatePlanController {

    private final CreatePlanInputBoundary interactor;

    public CreatePlanController(CreatePlanInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    public void execute(LocalDate date, List<String> mealSlotNames) {
        CreatePlanInputData inputData =
                new CreatePlanInputData(date, mealSlotNames);

        interactor.execute(inputData);
    }
}
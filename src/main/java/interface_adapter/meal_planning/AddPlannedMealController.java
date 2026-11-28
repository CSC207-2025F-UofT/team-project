package interface_adapter.meal_planning;

import use_case.meal_planning.AddPlannedMealInputBoundary;
import use_case.meal_planning.AddPlannedMealInputData;

import java.time.LocalDate;
import java.util.Objects;

public class AddPlannedMealController {

    private final AddPlannedMealInputBoundary interactor;

    public AddPlannedMealController(
            AddPlannedMealInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    public void loadOptions(LocalDate date) {
        interactor.loadOptions(date);
    }

    public void execute(LocalDate date, String mealSlotId,
                        String foodId, double grams) {
        interactor.execute(new AddPlannedMealInputData(
                date, mealSlotId, foodId, grams));
    }

    public void loadPortions(LocalDate date, String mealSlotId) {
        interactor.loadPortions(date, mealSlotId);
    }
}
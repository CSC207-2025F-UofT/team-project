package interface_adapter.edit_planned_meal;


import use_case.edit_planned_meal.ChangePlannedPortionInputBoundary;
import use_case.edit_planned_meal.ChangePlannedPortionInputData;

import java.time.LocalDate;
import java.util.Objects;

public class ChangePlannedPortionController {

    private final ChangePlannedPortionInputBoundary interactor;

    public ChangePlannedPortionController(
            ChangePlannedPortionInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    public void execute(LocalDate date, String mealSlotId,
                        int portionIndex, double newGrams) {
        interactor.execute(new ChangePlannedPortionInputData(
                date, mealSlotId, portionIndex, newGrams));
    }
}

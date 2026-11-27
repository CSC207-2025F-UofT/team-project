package use_case.meal_planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Input data for creating a daily meal plan.
 */
public class CreatePlanInputData {

    private final LocalDate date;
    private final List<String> mealSlotNames;

    public CreatePlanInputData(LocalDate date,
                               List<String> mealSlotNames) {
        this.date = date;
        this.mealSlotNames = mealSlotNames == null
                ? null
                : Collections.unmodifiableList(
                new ArrayList<>(mealSlotNames));
    }

    public LocalDate getDate() {
        return date;
    }

    public List<String> getMealSlotNames() {
        return mealSlotNames;
    }
}
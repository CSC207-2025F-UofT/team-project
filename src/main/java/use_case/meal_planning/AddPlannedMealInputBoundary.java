package use_case.meal_planning;
import java.time.LocalDate;
/**
 * Input boundary for creating a daily meal plan.
 */
public interface AddPlannedMealInputBoundary {

    void execute(AddPlannedMealInputData inputData);
    void loadOptions(LocalDate date);
}


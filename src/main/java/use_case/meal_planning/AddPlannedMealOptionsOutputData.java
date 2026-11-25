package use_case.meal_planning;

import java.time.LocalDate;
import java.util.List;

public class AddPlannedMealOptionsOutputData {

    private final LocalDate date;
    private final List<AddPlannedMealOption> mealSlots;
    private final List<AddPlannedMealOption> foods;

    public AddPlannedMealOptionsOutputData(
            LocalDate date,
            List<AddPlannedMealOption> mealSlots,
            List<AddPlannedMealOption> foods) {
        this.date = date;
        this.mealSlots = List.copyOf(mealSlots);
        this.foods = List.copyOf(foods);
    }

    public LocalDate getDate() {
        return date;
    }

    public List<AddPlannedMealOption> getMealSlots() {
        return mealSlots;
    }

    public List<AddPlannedMealOption> getFoods() {
        return foods;
    }
}
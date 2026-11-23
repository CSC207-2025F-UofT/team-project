package use_case.remove_planned_meal;

import entity.MealType;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Input data for removing a planned meal.
 */
public class RemovePlannedMealInputData {

    private final LocalDate weekStart;
    private final DayOfWeek day;
    private final MealType mealType;

    /**
     * Creates input data.
     *
     * @param weekStart any day of the week shown; the interactor moves it back to that week's Monday
     * @param day the day of the slot to clear
     * @param mealType the meal of the slot to clear
     */
    public RemovePlannedMealInputData(LocalDate weekStart, DayOfWeek day, MealType mealType) {
        this.weekStart = weekStart;
        this.day = day;
        this.mealType = mealType;
    }

    /**
     * Returns the week start.
     *
     * @return the week start
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }

    /**
     * Returns the day of the slot.
     *
     * @return the day
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * Returns the meal of the slot.
     *
     * @return the meal type
     */
    public MealType getMealType() {
        return mealType;
    }
}

package use_case.move_planned_meal;

import entity.MealType;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Input data for moving a planned meal.
 */
public class MovePlannedMealInputData {

    private final LocalDate weekStart;
    private final DayOfWeek fromDay;
    private final MealType fromMealType;
    private final DayOfWeek toDay;
    private final MealType toMealType;

    /**
     * Creates input data.
     *
     * @param weekStart any day of the week shown; the interactor moves it back to that week's Monday
     * @param fromDay the day of the slot the meal is in now
     * @param fromMealType the meal of the slot the meal is in now
     * @param toDay the day of the slot to move it to
     * @param toMealType the meal of the slot to move it to
     */
    public MovePlannedMealInputData(LocalDate weekStart, DayOfWeek fromDay, MealType fromMealType,
                                    DayOfWeek toDay, MealType toMealType) {
        this.weekStart = weekStart;
        this.fromDay = fromDay;
        this.fromMealType = fromMealType;
        this.toDay = toDay;
        this.toMealType = toMealType;
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
     * Returns the day of the source slot.
     *
     * @return the source day
     */
    public DayOfWeek getFromDay() {
        return fromDay;
    }

    /**
     * Returns the meal of the source slot.
     *
     * @return the source meal type
     */
    public MealType getFromMealType() {
        return fromMealType;
    }

    /**
     * Returns the day of the target slot.
     *
     * @return the target day
     */
    public DayOfWeek getToDay() {
        return toDay;
    }

    /**
     * Returns the meal of the target slot.
     *
     * @return the target meal type
     */
    public MealType getToMealType() {
        return toMealType;
    }
}

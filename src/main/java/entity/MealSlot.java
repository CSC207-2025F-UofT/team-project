package entity;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * One cell of the weekly meal plan grid: a day of the week and a meal of that day.
 * Immutable value object, so it is safe to use as a key in a Map.
 * Slots are ordered Monday breakfast first and Sunday dinner last.
 */
public final class MealSlot implements Comparable<MealSlot> {

    private static final List<MealSlot> ALL_SLOTS = buildAllSlots();

    private final DayOfWeek day;
    private final MealType mealType;

    /**
     * Creates a slot.
     * @param day the day of the week
     * @param mealType the meal of that day
     * @throws IllegalArgumentException if day or mealType is null
     */
    public MealSlot(DayOfWeek day, MealType mealType) {
        if (day == null || mealType == null) {
            throw new IllegalArgumentException("Day and meal type cannot be null");
        }
        this.day = day;
        this.mealType = mealType;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public MealType getMealType() {
        return mealType;
    }

    /**
     * Returns all 21 slots of a week (7 days x 3 meals), Monday breakfast first.
     * @return an unmodifiable list of every slot in order
     */
    public static List<MealSlot> allSlots() {
        return ALL_SLOTS;
    }

    private static List<MealSlot> buildAllSlots() {
        List<MealSlot> slots = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            for (MealType mealType : MealType.values()) {
                slots.add(new MealSlot(day, mealType));
            }
        }
        return Collections.unmodifiableList(slots);
    }

    @Override
    public int compareTo(MealSlot other) {
        int byDay = day.compareTo(other.day);
        if (byDay != 0) {
            return byDay;
        }
        return mealType.compareTo(other.mealType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealSlot)) {
            return false;
        }
        MealSlot other = (MealSlot) o;
        return day == other.day && mealType == other.mealType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, mealType);
    }

    @Override
    public String toString() {
        return day + " " + mealType;
    }
}

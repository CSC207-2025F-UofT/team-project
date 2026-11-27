package use_case.meal_planning;

import java.time.LocalDate;

public class AddPlannedMealInputData {

    private final LocalDate date;
    private final String mealSlotId;
    private final String foodId;
    private final double grams;

    public AddPlannedMealInputData(LocalDate date, String mealSlotId,
                                   String foodId, double grams) {
        this.date = date;
        this.mealSlotId = mealSlotId;
        this.foodId = foodId;
        this.grams = grams;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMealSlotId() {
        return mealSlotId;
    }

    public String getFoodId() {
        return foodId;
    }

    public double getGrams() {
        return grams;
    }
}
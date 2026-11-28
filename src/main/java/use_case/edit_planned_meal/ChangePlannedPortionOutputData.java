package use_case.edit_planned_meal;

import java.time.LocalDate;

public class ChangePlannedPortionOutputData {

    private final LocalDate date;
    private final String mealId;
    private final String mealSlotId;
    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fat;

    public ChangePlannedPortionOutputData(
            LocalDate date, String mealId, String mealSlotId,
            double calories, double protein, double carbs, double fat) {
        this.date = date;
        this.mealId = mealId;
        this.mealSlotId = mealSlotId;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMealId() {
        return mealId;
    }

    public String getMealSlotId() {
        return mealSlotId;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }
}